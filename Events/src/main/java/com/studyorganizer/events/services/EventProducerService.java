package com.studyorganizer.events.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EventProducerService {

    private static final String TOPIC_REQUEST_USERS = "request_users_topic";

    @Autowired
    private KafkaTemplate<String, List<Long>> kafkaTemplate;

    public CompletableFuture<SendResult<String, List<Long>>> requestUsersByEventId(String eventId, List<Long> attendeeIds) {
        return kafkaTemplate.send(TOPIC_REQUEST_USERS, eventId, attendeeIds);
    }
}
