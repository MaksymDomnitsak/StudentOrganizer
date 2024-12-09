package com.studyorganizer.studgroups.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDtoResponse {
    Long id;
    String firstName;
    String lastName;
    String patronymicName;
    String email;
    String phoneNumber;
    GroupDto group;
}
