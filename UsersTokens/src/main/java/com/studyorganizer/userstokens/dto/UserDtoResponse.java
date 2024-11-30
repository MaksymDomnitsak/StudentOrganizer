package com.studyorganizer.userstokens.dto;

import com.studmodel.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoResponse {
    Long id;
    String firstName;
    String lastName;
    String patronymicName;
    String email;
    UserRole userRole;
}

