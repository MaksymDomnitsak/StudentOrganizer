package com.studyorganizer.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupedNote {
    String title;
    Long schedule;
    Long[] students;
    String body;
}