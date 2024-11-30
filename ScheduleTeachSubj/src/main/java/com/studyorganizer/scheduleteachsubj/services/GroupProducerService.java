package com.studyorganizer.scheduleteachsubj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GroupProducerService {
    private static final String TOPIC_REQUEST_GROUPS = "request_groups_topic";

    @Autowired
    private KafkaTemplate<String, String> groupKafkaTemplate;

    public CompletableFuture<SendResult<String, String>> requestGroupIdByGroupTitle(String groupId, String groupTitle) {
        return groupKafkaTemplate.send(TOPIC_REQUEST_GROUPS, groupId, groupTitle);
    }

}
