package com.studyorganizer.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    String firstName;
    String lastName;
    String patronymicName;
    String email;
}
