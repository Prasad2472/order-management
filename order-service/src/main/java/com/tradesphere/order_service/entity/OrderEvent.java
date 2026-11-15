package com.tradesphere.order_service.entity;

import com.tradesphere.order_service.model.EventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "order_events")
public class OrderEvent {
    @Id
    @Column(name = "event_id")
    private String eventId;
    @Column(name = "payload", columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(name = "event_status")
    private String eventStatus;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "sent_at")
    private Instant sentAt;

    public static OrderEvent toEvent(String payload, EventStatus eventStatus) {
        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventStatus(eventStatus.name());
        event.setPayload(payload);
        event.setSentAt(Instant.now());
        event.setCreatedAt(Instant.now());
        return event;
    }

    public void markSent() {
        this.setEventStatus(EventStatus.SENT.name());
        this.setSentAt(Instant.now());
    }

}
