package com.studyorganizer.scheduleteachsubj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    Long id;
    String name;
//    List<StudentDto> students;
    boolean disabled;
}
