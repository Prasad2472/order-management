package com.tradesphere.order_service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradesphere.order_service.entity.OrderEvent;
import com.tradesphere.order_service.model.EventStatus;
import com.tradesphere.order_service.repository.OrderEventRepository;
import com.tradesphere.order_service.utility.OrderServiceUtility;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@Component
public class PublishEvents {
    private static Logger logger = LoggerFactory.getLogger(PublishEvents.class);
    @Autowired
    private OrderEventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    @Qualifier("eventPublisher")
    private ExecutorService executorService;

    @Scheduled(fixedDelay = 5000L)
    public void publish() {
        executorService.submit(this::publishEvent);
    }

    private void publishEvent() {
        List<OrderEvent> events = eventRepository.fetchPendingEvents();
        logger.info("Publishing events: " + events.size());
        for (OrderEvent event : events) {
            String payload = null;
            try {
                payload = objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            kafkaTemplate.
                    send(OrderServiceUtility.ORDER_TOPIC, event.getEventId(), payload).
                    whenComplete(((result, throwable) -> {
                        if (throwable == null) {
                            markSent(event);
                        } else {
                            logger.warn("Publish event failed, will try again, event id:" + event.getEventId());
                        }
                    }));
        }
    }

    @Transactional
    public void markSent(OrderEvent event) {
        event.markSent();
        eventRepository.save(event);
    }
}
