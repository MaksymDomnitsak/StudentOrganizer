package com.studyorganizer.studgroups.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SemesterResponse {
    private List<Group> semester_groups;

    public List<Group> getSemester_groups() {
        return semester_groups;
    }

    public void setSemester_groups(List<Group> semester_groups) {
        this.semester_groups = semester_groups;
    }

    @Data
    public static class Group {
        private int id;
        private Boolean disable;
        private String title;


    }
}
