package com.tradesphere.order_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradesphere.order_service.entity.OrderEntity;
import com.tradesphere.order_service.entity.OrderEvent;
import com.tradesphere.order_service.exception.OrderNotFoundException;
import com.tradesphere.order_service.model.EventStatus;
import com.tradesphere.order_service.model.Order;
import com.tradesphere.order_service.repository.OrderEventRepository;
import com.tradesphere.order_service.repository.OrderRepository;
import com.tradesphere.order_service.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository repository;
    private OrderEventRepository eventRepository;
    private ObjectMapper objectMapper;

    @Transactional
    @Override
    public Order createOrder(Order order) {
        OrderEntity entity = repository.save(mapToOrderEntity(null, order));
        Order saved = mapToOrder(entity);
        //Save created order to publish
        eventRepository.save(OrderEvent.toEvent(toJSON(saved), EventStatus.PENDING));
        return mapToOrder(entity);

    }

    private String toJSON(Order saved) {
        String payload = null;
        try {
            payload = objectMapper.writeValueAsString(saved);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return payload;
    }

    @Transactional
    @Override
    public Order updateOrder(Order order) {
        OrderEntity entity = repository.findByOrderID(order.getOrderID())
                .orElseThrow(() -> new OrderNotFoundException("No Order found with order ID:" + order.getOrderID()));
        OrderEntity saved = repository.save(mapToOrderEntity(entity, order));
        Order savedOrder = mapToOrder(saved);

        //Save updated order
        eventRepository.save(OrderEvent.toEvent(toJSON(savedOrder), EventStatus.PENDING));
        return savedOrder;
    }

    @Override
    public Order fetchOrder(UUID orderId) {
        OrderEntity entity = repository.findByOrderID(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No Order found with order ID:" + orderId));

        return mapToOrder(entity);
    }

    private OrderEntity mapToOrderEntity(OrderEntity entity, Order order) {
        if (entity == null) {
            entity = new OrderEntity();
        }
        entity.setOrderID(order.getOrderID());
        entity.setOrderStatus(order.getOrderStatus());
        entity.setPrice(order.getPrice());
        entity.setQuantity(order.getQuantity());
        entity.setProductId(order.getProductId());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        return entity;
    }

    private Order mapToOrder(OrderEntity entity) {
        Order order = new Order();
        order.setOrderID(entity.getOrderID());
        order.setOrderStatus(entity.getOrderStatus());
        order.setPrice(entity.getPrice());
        order.setQuantity(entity.getQuantity());
        order.setProductId(entity.getProductId());
        order.setUpdatedAt(entity.getUpdatedAt());
        order.setCreatedAt(entity.getCreatedAt());
        return order;
    }
}
