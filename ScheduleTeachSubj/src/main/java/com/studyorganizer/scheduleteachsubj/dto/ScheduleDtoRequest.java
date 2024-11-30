package com.studyorganizer.scheduleteachsubj.dto;


import com.studmodel.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDtoRequest {
    String customTitle;

    Long subjectId;

    Long creatorId;

    Long groupId;

    Long dayOfWeek;

    Boolean isEvenWeek;

    Long lessonOrder;

    LessonType typeOfLesson;

    Boolean isOnline;

    String auditoryNumber;

}
