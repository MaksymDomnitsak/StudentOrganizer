package com.studyorganizer.studgroups.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDtoRequest {
    String firstName;
    String lastName;
    String patronymicName;
    String email;
    Long groupId;
}
