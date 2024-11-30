package com.studyorganizer.userstokens.services;

import com.studmodel.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserConsumerService {

    private static final String TOPIC_RESPONSE_USERS = "response_users_topic";

    @Autowired
    private KafkaTemplate<String, List<User>> kafkaTemplate;

    @Autowired
    private UserService userService;

    @KafkaListener(topics = "request_users_topic", groupId = "user_service_group", autoStartup = "true")
    public void fetchUsersByEventId(@Header(KafkaHeaders.RECEIVED_KEY)String eventId, List<Long> attendeeIds) {
        List<User> users = attendeeIds.stream()
                .map(userService::getUserById)
                .collect(Collectors.toList());
        kafkaTemplate.send(TOPIC_RESPONSE_USERS, eventId, users);
    }
}
