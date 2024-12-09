package com.studyorganizer.notes.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDtoRequest {

    String title;

    Long lesson_id;

    Long student_id;

    String body;

    @JsonProperty("isFinished")
    Boolean finished;


}
