package com.studyorganizer.googleschedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    Long teacherId;
    Boolean setOnline;
    Boolean setTopic;
}
