package com.colibrihub.CommerceCore.service.Impl;

import com.colibrihub.CommerceCore.Exception.InvalidOperationException;
import com.colibrihub.CommerceCore.Exception.ResourceNotFoundException;
import com.colibrihub.CommerceCore.dto.Request.OrderItemRequest;
import com.colibrihub.CommerceCore.dto.Request.OrderRequest;
import com.colibrihub.CommerceCore.dto.Response.OrderResponse;
import com.colibrihub.CommerceCore.dto.Request.OrderStatusRequest;
import com.colibrihub.CommerceCore.dto.Response.PagedResponse;
import com.colibrihub.CommerceCore.entity.OrderItem;
import com.colibrihub.CommerceCore.entity.Product;
import com.colibrihub.CommerceCore.enums.OrderStatus;
import com.colibrihub.CommerceCore.event.OrderConfirmedEvent;
import com.colibrihub.CommerceCore.event.ProductItemEvent;
import com.colibrihub.CommerceCore.mapper.OrderItemMapper;
import com.colibrihub.CommerceCore.mapper.OrderMapper;
import com.colibrihub.CommerceCore.repository.OrderRepository;
import com.colibrihub.CommerceCore.repository.ProductRepository;
import com.colibrihub.CommerceCore.service.InvoiceService;
import com.colibrihub.CommerceCore.service.OrderEventPublisher;
import com.colibrihub.CommerceCore.service.OrderService;
import com.colibrihub.CommerceCore.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final InvoiceService invoiceService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderEventPublisher publisher;

    public OrderServiceImpl(
            ProductService productService, InvoiceService invoiceService,
            OrderRepository orderRepository, ProductRepository productRepository,
            OrderMapper orderMapper, OrderItemMapper orderItemMapper,
            OrderEventPublisher publisher) {
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.publisher = publisher;
    }

    @Override
    public PagedResponse<OrderResponse> findByClientId(Pageable pageable, Long clientId) {
        var orderPage = orderRepository.findByClientId(pageable, clientId).map(orderMapper::toResponse);
        return PagedResponse.of(orderPage);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatusRequest req) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.DELIVERED ||
                order.getOrderStatus() == OrderStatus.CANCELLED ||
                order.getOrderStatus() == OrderStatus.SHIPPED) {
            throw new InvalidOperationException("Can't Change Order Status from " + order.getOrderStatus());
        }

        if (req.getOrderStatus() == OrderStatus.CANCELLED) {
            order.getOrderItems().forEach(item ->
                productService.addQuantityToStock(item.getProduct(), item.getQuantity()));
        }

        if (req.getOrderStatus() == OrderStatus.CONFIRMED) {
            invoiceService.createInvoice(order);

            List<ProductItemEvent> products = new ArrayList<>();

            order.getOrderItems().forEach(item -> {
                var productEventItem = orderItemMapper.toProductItemEvent(item);
                products.add(productEventItem);
            });

            var event = new OrderConfirmedEvent(order, products);
            publisher.publishConfirmedOrder(event);
        }

        order.setOrderStatus(req.getOrderStatus());

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest req) {

        List<Long> ids = extractOrderItemsIds(req.getItems());
        var products = listToProductMap(productRepository.findAllById(ids));

        if (products.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        var orderItems = new ArrayList<OrderItem>();

        req.getItems().forEach(item -> {
            var product = products.get(item.getProductId());

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new InvalidOperationException("Insufficient stock for product: " + product.getSku());
            }

            productService.subtractQuantityFromStock(product, item.getQuantity());

            var orderItem = orderItemMapper.toEntity(item, product);
            orderItems.add(orderItem);
        });

        var total = calculateOrderTotal(orderItems);
        var order = orderMapper.toEntity(req, total);

        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    private List<Long> extractOrderItemsIds(List<OrderItemRequest> orderItemsReq) {
        return orderItemsReq.stream().map(OrderItemRequest::getProductId).toList();
    }

    private Map<Long, Product> listToProductMap(List<Product> products) {
        return products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    private BigDecimal calculateOrderTotal(List<OrderItem> orderItems) {
        var subtotal = orderItems.stream().map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var iva = subtotal.multiply(BigDecimal.valueOf(0.13));
        return subtotal.add(iva);
    }
}