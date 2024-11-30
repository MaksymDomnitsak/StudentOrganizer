package com.studyorganizer.studgroups.services;

import com.studmodel.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
public class GroupConsumerService {
    private static final String TOPIC_RESPONSE_GROUPS = "response_groups_topic";

    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    @Autowired
    private GroupService groupService;

    @KafkaListener(topics = "request_groups_topic", groupId = "group_service_group", autoStartup = "true")
    public void fetchUsersByEventId(@Header(KafkaHeaders.RECEIVED_KEY)String groupId, String groupTitle) {
        Group group = groupService.getGroupByName(groupTitle);
        kafkaTemplate.send(TOPIC_RESPONSE_GROUPS, groupId, group.getId());
    }
}
