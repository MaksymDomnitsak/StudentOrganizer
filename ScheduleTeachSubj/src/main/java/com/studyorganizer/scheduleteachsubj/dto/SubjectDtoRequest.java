package com.studyorganizer.scheduleteachsubj.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectDtoRequest {
    String name;

    public SubjectDtoRequest(String name) {
        this.name = name;
    }

    public SubjectDtoRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
