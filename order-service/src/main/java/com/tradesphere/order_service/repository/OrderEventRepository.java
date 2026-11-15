package com.tradesphere.order_service.repository;

import com.tradesphere.order_service.entity.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OrderEventRepository extends JpaRepository<OrderEvent, String> {
    @Query("""
                SELECT e FROM OrderEvent e
                WHERE e.eventStatus = 'PENDING'
                ORDER BY e.createdAt
            """)
    List<OrderEvent> fetchPendingEvents();

    @Modifying
    @Query("""
      DELETE FROM OrderEvent e
      WHERE e.eventStatus = 'SENT'
      AND e.sentAt < :cutoff
    """)
    int deleteSentBefore(@Param("cutoff") Instant cutoff);
}
