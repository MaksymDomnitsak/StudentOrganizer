package com.studyorganizer.scheduleteachsubj.mappers;

import com.studmodel.Subject;
import com.studyorganizer.scheduleteachsubj.dto.SubjectDtoRequest;
import com.studyorganizer.scheduleteachsubj.dto.SubjectDtoResponse;
import org.mapstruct.Mapper;

@Mapper
public interface SubjectDtoToSubjectMapper {
    Subject subjectDtoToSubject(SubjectDtoRequest subjectDto);

    SubjectDtoResponse subjectToSubjectDto(Subject subject);
}
