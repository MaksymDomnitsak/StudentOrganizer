package com.studyorganizer.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoRequest {
    Long id;

    String title;

    LocalDateTime startTime;

    LocalDateTime endTime;

    Long subject;

    Long creator;

    List<Long> attendees;

    @JsonProperty("isOnline")
    Boolean isOnline;

    String auditoryNumber;

}
