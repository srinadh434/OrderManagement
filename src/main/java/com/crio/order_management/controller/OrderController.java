
package com.crio.order_management.controller;

import com.crio.order_management.dto.OrderRequest;
import com.crio.order_management.entity.Order;
import com.crio.order_management.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create order
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest request) {

        Order createdOrder = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long id) {

        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderRequest request) {

        return ResponseEntity.ok(
                orderService.updateOrder(id, request)
        );
    }

}
