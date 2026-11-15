package com.tradesphere.order_service.controller;

import com.tradesphere.order_service.model.Order;
import com.tradesphere.order_service.model.OrderStatus;
import com.tradesphere.order_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        logger.info("Create  an order: {}", order);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderID(UUID.randomUUID());
        order.setCreatedAt(Instant.now());
        Order saved = orderService.createOrder(order);
        logger.info("Saved order {}", saved);
        return ResponseEntity.ok().body(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody Order order) {
        logger.info("Updating the order: {}", order);
        order.setOrderStatus(OrderStatus.UPDATED);
        order.setUpdatedAt(Instant.now());
        Order saved = orderService.updateOrder(order);
        logger.info("Updated order: {}", saved);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> fetchOrder(@PathVariable("orderId") UUID orderId) {
        logger.info("Fetching Order ID: {}", orderId);
        Order order = orderService.fetchOrder(orderId);
        logger.info("Order fetched: {}", order);
        return ResponseEntity.ok().body(order);
    }
}