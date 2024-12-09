package com.studyorganizer.notes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studmodel.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDtoResponse {
    Long id;

    SubjectDtoResponse subject;

    Long dayOfWeek;

    @JsonProperty("isEvenWeek")
    boolean isEvenWeek;

    LessonType typeOfLesson;

}
