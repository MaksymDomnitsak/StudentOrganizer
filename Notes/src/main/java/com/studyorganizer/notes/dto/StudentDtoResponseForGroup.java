package com.studyorganizer.notes.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
public class StudentDtoResponseForGroup {
    Long id;
    String firstName;
    String lastName;
    String patronymicName;
    String email;
    String phoneNumber;
}
