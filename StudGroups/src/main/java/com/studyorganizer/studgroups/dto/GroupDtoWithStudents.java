package com.studyorganizer.studgroups.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class GroupDtoWithStudents {
    Long id;

    String name;

    List<StudentDtoResponseForGroup> students;

}
