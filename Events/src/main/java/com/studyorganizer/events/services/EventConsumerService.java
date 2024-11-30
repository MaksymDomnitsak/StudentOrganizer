package com.studyorganizer.events.services;

import com.studmodel.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumerService {

    private final Map<String, List<User>> usersCache = new HashMap<>();

    @KafkaListener(topics = "response_users_topic", groupId = "event_service_group",autoStartup = "true")
    public void receiveUsers(@Header(KafkaHeaders.RECEIVED_KEY) String key, List<User> users) {
        usersCache.put(key, users);
    }

    public List<User> getUsersFromCache(Long id) {
        return usersCache.get(id.toString());
    }
}
