package com.tradesphere.order_service.service;

import com.tradesphere.order_service.model.Order;

import java.util.UUID;

public interface OrderService {
    Order createOrder(Order order);

    Order updateOrder(Order order);

    Order fetchOrder(UUID orderId);

}
