package com.studyorganizer.scheduleteachsubj.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;

}
