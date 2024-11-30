package com.studyorganizer.googleschedule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ScheduleProducerService {
    private static final String TOPIC_REQUEST_SCHEDULE = "request_schedule_topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> requestScheduleByTeacherId(String teacherId, String isEven) {
        return kafkaTemplate.send(TOPIC_REQUEST_SCHEDULE, teacherId, isEven);
    }
}
