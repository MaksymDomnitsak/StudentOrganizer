package com.studyorganizer.studgroups.mappers;

import com.studmodel.Student;
import com.studyorganizer.studgroups.dto.StudentDtoRequest;
import com.studyorganizer.studgroups.dto.StudentDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student studentDtoToStudent(StudentDtoRequest studentDto);

    StudentDtoResponse studentToStudentDto(Student student);
}
