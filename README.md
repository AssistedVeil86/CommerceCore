# CommerceCore

> A robust business engine for E-Commerce applications, responsible for managing Products, Orders, and Invoices with seamless WooCommerce integration

## 📋 Overview

**CommerceCore** is a powerful business engine designed to serve as the backbone of E-Commerce applications. Built with Java 21 and Spring Boot 3.5.15, it seamlessly integrates with WooCommerce to provide comprehensive product synchronization, order processing, and automated invoice generation.

This API handles the main core of the system, ensuring efficient data synchronization, smart caching strategies, and scalable operations for modern e-commerce platforms.

---

## 🛠️ Technologies Used

### Core Framework
- **Java 21** — Latest LTS Java version with modern language features
- **Spring Boot 3.5.15** — Enterprise-grade application framework
- **Spring Data JPA** — Simplified and powerful data access layer

### Data & Caching
- **MySQL** — Relational database for persistent storage
- **Redis** — High-performance in-memory caching layer

### Integration & Messaging
- **WebClient** — Reactive HTTP client for WooCommerce API communication
- **RabbitMQ** — Message broker for asynchronous event processing
- **WordPress / WooCommerce** — E-commerce platform integration

### Infrastructure & Documentation
- **Docker** — Containerization for consistent and easy deployment
- **Swagger / OpenAPI** — Interactive and auto-generated API documentation

---

## ✨ Features

As the core business engine of the system, CommerceCore provides the following capabilities:

### 🔄 Inventory Syncing with WooCommerce
Automatically syncs products from WooCommerce using a scheduled **WebClient** task that runs **every 5 minutes**, keeping the local inventory stored via Spring Data JPA always up-to-date and in sync.

### 📦 Paged and Cached Inventory for Quick Access
The synced inventory is persisted in a **MySQL** database and retrieved using **Pageable** queries for scalable handling of large datasets. Frequently accessed data is stored in **Redis Cache** for fast and efficient retrieval.

### 🛒 Order Management
Allows clients to create orders with their desired products and tracks the order status throughout its entire lifecycle:

| Status | Description |
|---|---|
| `PENDING` | Order has been placed and awaits confirmation |
| `CONFIRMED` | Order has been confirmed and is being prepared |
| `CANCELLED` | Order has been cancelled |
| `SHIPPED` | Order is on its way to the client |
| `DELIVERED` | Order has been successfully delivered |

### 📋 Order History for Clients
Clients can view their complete order history with all related details. Results are handled with **Pageable** for large data sets and cached with **Redis** for quick access.

### 🧾 Invoice Generation upon Order Confirmation
Whenever an order is confirmed, an invoice is automatically generated and stored in the database, including:
- Subtotal
- Tax
- Total
- Client information

Clients can retrieve the invoice associated with any specific order at any time.

### 📊 Inventory Management
Inventory levels are automatically adjusted throughout the order lifecycle:
- **Stock is decremented** when an order is initially created
- **Stock is restored** when an order is cancelled
- **Redis cache is invalidated** after every inventory update to ensure data consistency

### ⚠️ Low Stock Alerts
A dedicated endpoint allows owners and administrators to query all products whose stock falls **below 5 units**, enabling proactive restocking before items run out.

---

## 🏗️ Building Process

The development process was relatively quick to execute. The main challenge was designing the overall application flow and thinking through how different actions should trigger subsequent processes throughout the system. It required careful planning upfront, but proved to be a great architectural exercise that reinforced event-driven thinking and system design skills.

---

## 🚀 How to Run the Project

### Prerequisites
Make sure you have the following installed:
- [Java 21](https://openjdk.org/)
- [Docker](https://www.docker.com/) & Docker Compose
- A running **WooCommerce** instance with API access

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/CommerceCore.git
   cd CommerceCore
   ```

2. **Configure environment variables**

   Update `application.properties` or `application.yml` with your credentials:
   ```properties
   # Database
   spring.datasource.url=jdbc:mysql://localhost:3306/commercecore
   spring.datasource.username=your_user
   spring.datasource.password=your_password

   # Redis
   spring.data.redis.host=localhost
   spring.data.redis.port=6379

   # WooCommerce
   woocommerce.base-url=https://your-store.com
   woocommerce.consumer-key=your_consumer_key
   woocommerce.consumer-secret=your_consumer_secret

   # RabbitMQ
   spring.rabbitmq.host=localhost
   spring.rabbitmq.port=5672
   ```

3. **Start infrastructure services with Docker**
   ```bash
   docker-compose up -d
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the API documentation**

   Once the app is running, navigate to:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

   The API is now up and running! 🎉

---
