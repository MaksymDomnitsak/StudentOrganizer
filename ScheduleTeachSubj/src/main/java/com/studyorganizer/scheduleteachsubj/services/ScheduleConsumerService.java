package com.studyorganizer.scheduleteachsubj.services;

import com.studmodel.Schedule;
import com.studyorganizer.scheduleteachsubj.repositories.ScheduleRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleConsumerService {

    private final KafkaTemplate<String, List<Schedule>> kafkaTemplate;

    @Autowired
    ScheduleRepository scheduleRepository;

    public ScheduleConsumerService(KafkaTemplate<String, List<Schedule>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "request_schedule_topic", groupId = "schedule_service_group", containerFactory = "secondKafkaListenerContainerFactory", autoStartup = "true")
    public void processMessage(@Header(KafkaHeaders.RECEIVED_KEY)String teacherId, String isEven) {
        List<Schedule> list = scheduleRepository.findAllByTeacherIdAndEvenWeek(Long.parseLong(teacherId),Boolean.parseBoolean(isEven));
        list.stream()
                .forEach(schedule -> {
                    if (schedule.getGroup() != null) {
                        schedule.getGroup().setStudents(null);
                    }
                });

        kafkaTemplate.send(new ProducerRecord<>("response_schedule_topic",teacherId, list));
    }
}
