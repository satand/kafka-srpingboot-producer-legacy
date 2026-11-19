package com.kafka;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class TestKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(TestKafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final AtomicLong counter = new AtomicLong();

    public TestKafkaProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Scheduled(fixedDelayString = "${app.kafka.send-interval-ms:5000}")
    public void send() {
        final String key = UUID.randomUUID().toString();
        final long seq = counter.incrementAndGet();
        final String payload = "msg-" + seq + " @ " + Instant.now();

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, payload);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Inviato -> key={} | value={} <- topic={}, partition={}, offset={}",
                        key,
                        payload,
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Errore invio -> key={} | value={} | topic={}", key, payload, topic, ex);
            }
        });
    }
}
