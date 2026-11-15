package com.tradesphere.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int productId;
    private double price;
    private int quantity;
    private UUID orderID;
    private OrderStatus orderStatus;
    private Instant createdAt;
    private Instant updatedAt;

}
