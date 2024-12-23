package com.studyorganizer.scheduleteachsubj.dto;

public class SubjectDtoResponse {

    Long id;

    String name;

    public SubjectDtoResponse() {
    }

    public SubjectDtoResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
