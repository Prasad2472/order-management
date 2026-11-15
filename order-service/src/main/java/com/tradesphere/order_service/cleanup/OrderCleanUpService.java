package com.tradesphere.order_service.cleanup;

import com.tradesphere.order_service.repository.OrderEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OrderCleanUpService {
    @Autowired
    private OrderEventRepository eventRepository;
    private static Logger logger = LoggerFactory.getLogger(OrderCleanUpService.class);

    @Async
    //  @Scheduled(cron = "0 0 */6 * * *") // every 6 hours
    @Scheduled(cron = "0 0/1 * * * *") // every 1 minutes
    @Transactional
    public void cleanup() {

        Instant cutoff = Instant.now()
                .minus(30, ChronoUnit.MINUTES);

        int deleted = eventRepository.deleteSentBefore(cutoff);

        logger.info("Event cleanup deleted {} rows", deleted);
    }
}
