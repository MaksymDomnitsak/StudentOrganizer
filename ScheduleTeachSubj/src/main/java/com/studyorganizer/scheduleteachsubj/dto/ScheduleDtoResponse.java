package com.studyorganizer.scheduleteachsubj.dto;

import com.studmodel.LessonType;
import com.studmodel.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDtoResponse {
    Long id;

    SubjectDtoResponse subject;

    TeacherDtoResponse teacher;

    GroupDto group;

    Long dayOfWeek;

    boolean isEvenWeek;

    Long lessonOrder;

    LessonType typeOfLesson;

    boolean isOnline;

    String auditoryNumber;

}
