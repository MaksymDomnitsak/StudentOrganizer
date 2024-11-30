package com.studyorganizer.scheduleteachsubj.mappers;


import com.studmodel.Teacher;
import com.studyorganizer.scheduleteachsubj.dto.TeacherDtoRequest;
import com.studyorganizer.scheduleteachsubj.dto.TeacherDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherToTeacherDtoMapper {

    Teacher teacherDtoToTeacher(TeacherDtoRequest teacherDto);
     TeacherDtoResponse teacherToTeacherDto(Teacher teacher);
}
