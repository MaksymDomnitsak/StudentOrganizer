package com.studyorganizer.notes.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDtoResponse {

    Long id;
    String title;

    ScheduleDtoResponse lesson;

    StudentDtoResponseForGroup  student;

    String body;

    boolean isFinished;

}
