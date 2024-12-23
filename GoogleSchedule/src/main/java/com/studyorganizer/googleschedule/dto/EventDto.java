package com.studyorganizer.googleschedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String summary;
    private String location;
    private String description;
    private String frequency;
    private String repeats;
    private String[] attendeesEmails;
    private String conference;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long eventId;
}
