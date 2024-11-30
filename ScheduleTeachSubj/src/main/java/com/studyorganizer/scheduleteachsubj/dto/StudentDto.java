package com.studyorganizer.scheduleteachsubj.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    Long id;
    String email;
    String firstName;
    String lastName;
    String patronymicName;

}
