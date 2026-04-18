package com.colibrihub.CommerceCore.controller;

import com.colibrihub.CommerceCore.dto.Response.*;
import com.colibrihub.CommerceCore.service.ProductService;
import com.colibrihub.CommerceCore.service.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@Tag(name = "Products", description = "Gestión de productos")
public class ProductController {

    private final ProductService productService;
    private final SyncService syncService;

    public ProductController(ProductService productService, SyncService syncService) {
        this.productService = productService;
        this.syncService = syncService;
    }

    @GetMapping
    @Operation(
            summary = "Obtener lista de productos paginados.",
            description = "Obtener una lista paginada de 20 productos.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Productos Paginados Obtenidos Exitosamente!")
    })
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getProducts(
            @Parameter(description = "Número Actual de Página", example = "0")
            @RequestParam(defaultValue = "0", required = true) int page,

            @Parameter(description = "Cantidad de Productos", example = "20")
            @RequestParam(defaultValue = "20", required = true) int size) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(size, 100));
        var products = productService.getProducts(pageable);

        return ResponseEntity.ok(ApiResponse.success("Products Retrieved", products));
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Obtener producto por ID.",
            description = "Obtener un producto deseado mediante su ID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Producto Obtenido Exitosamente!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "Producto No Encontrado", content = {
                    @Content(mediaType = "application/problem+json")
            })
    })
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        var product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product Retrieved", product));
    }

    @PostMapping("sync")
    @Operation(
            summary = "Sincronizar productos con WooCommerce",
            description = "Sincronizar productos de forma manual con WooCommerce")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Productos Sincronizados Exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "Credenciales de WooCommerce Inválidas", content = {
                    @Content(mediaType = "application/problem+json")
            }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "502", description = "WooCommerce No Disponible", content = {
                    @Content(mediaType = "application/problem+json")
            }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Respuesta inválida de WooCommerce", content = {
                    @Content(mediaType = "application/problem+json")
            })
    })
    public ResponseEntity<ApiResponse<SyncResponse>> syncProducts(){
        var response = syncService.syncProducts();
        return ResponseEntity.ok(ApiResponse.success("Products Synced!", response));
    }
}