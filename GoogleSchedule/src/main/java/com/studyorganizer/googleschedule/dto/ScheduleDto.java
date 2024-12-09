package com.studyorganizer.googleschedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private String summary;
    private String location;
    private String description;
    private String startDate;
    private String frequency;
    private int repeats;
    private String[] attendeesEmails;
    private String conference;
    private String startTime;
    private String endTime;
    private Long scheduleId;

}
