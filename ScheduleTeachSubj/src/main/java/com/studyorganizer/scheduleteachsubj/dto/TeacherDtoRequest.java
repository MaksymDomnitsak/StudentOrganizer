package com.studyorganizer.scheduleteachsubj.dto;

import com.studmodel.UserRole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeacherDtoRequest{
    String firstName;
    String lastName;
    String middleName;
    String email;
    String phoneNumber;
    UserRole userRole;
    Boolean isEventer;
}
