package com.studyorganizer.googleschedule.services;

import com.studmodel.Schedule;
import com.studmodel.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleConsumerService {
    private final Map<String, List<Schedule>> scheduleCache = new HashMap<>();

    @KafkaListener(topics = "response_schedule_topic", groupId = "schedule_service_group",autoStartup = "true")
    public void receiveSchedule(@Header(KafkaHeaders.RECEIVED_KEY) String teacherKey, List<Schedule> schedule) {
        scheduleCache.put(teacherKey, schedule);
    }

    public List<Schedule> getScheduleFromCache(String id) {
        return scheduleCache.get(id);
    }
}
