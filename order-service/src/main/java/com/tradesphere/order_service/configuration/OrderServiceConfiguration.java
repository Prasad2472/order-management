package com.tradesphere.order_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class OrderServiceConfiguration {

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name("tradesphere.order.created-topic")
                .partitions(6)
                .replicas(3)   // use >=3 in prod
                .config("cleanup.policy", "delete")
                .config("retention.ms", "604800000") // 7 days
                .build();
    }

    @Bean(name = "eventPublisher")
    public ExecutorService eventPublisherPool() {
        ThreadFactory factory = Thread.ofVirtual().name("event-publisher-", 1).factory();
        ExecutorService executorService = Executors.newFixedThreadPool(4, factory);

        return executorService;
    }

    @Bean(name = "eventCleanUp")
    public ExecutorService cleanUpPool() {
        ThreadFactory factory = Thread.ofVirtual().name("event-cleanup-", 1).factory();
        ExecutorService executorService = Executors.newFixedThreadPool(1, factory);

        return executorService;
    }
}
