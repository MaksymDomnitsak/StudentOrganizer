package com.studyorganizer.scheduleteachsubj.services;

import com.studmodel.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupConsumerService {
    private final Map<String, Long> groupsCache = new HashMap<>();

    @KafkaListener(topics = "response_groups_topic", groupId = "schedule_group_id",containerFactory = "firstKafkaListenerContainerFactory",autoStartup = "true")
    public void receiveUsers(@Header(KafkaHeaders.RECEIVED_KEY) String key, Long groupId) {
       groupsCache.put(key, groupId);
    }

    public Long getGroupIdFromCache(String id) {
        return groupsCache.get(id);
    }
}
