package com.studyorganizer.events.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studmodel.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoResponse {
    Long id;

    String title;

    LocalDateTime startTime;

    LocalDateTime endTime;

    Subject subject;

    UserDto creator;

    List<UserDto> attendees;

    @JsonProperty("isOnline")
    boolean isOnline;

    String auditoryNumber;
}
