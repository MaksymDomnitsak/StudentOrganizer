package com.studyorganizer.scheduleteachsubj.services;

import com.studmodel.Teacher;
import com.studmodel.UserRole;
import com.studyorganizer.scheduleteachsubj.dto.TeacherDTO;
import com.studyorganizer.scheduleteachsubj.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Teacher getTeacherByFullName(String firstName, String lastName, String patronymic) {
        return teacherRepository.findByFirstNameAndLastNameAndAndPatronymicName(firstName, lastName, patronymic);
    }

    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher createTeacher(TeacherDTO teacherDto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(teacherDto.getName());
        teacher.setLastName(teacherDto.getSurname());
        teacher.setPatronymicName(teacherDto.getPatronymic());
        teacher.setCreatedAt(Instant.now());
        teacher.setUserRole(UserRole.TEACHER);
        teacher.setEventer(true);
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        return teacherRepository.findById(id).map(teacher -> {
            teacher.setEmail(teacherDetails.getEmail());
            teacher.setFirstName(teacherDetails.getFirstName());
            teacher.setLastName(teacherDetails.getLastName());
            teacher.setPatronymicName(teacherDetails.getPatronymicName());
            teacher.setPhoneNumber(teacherDetails.getPhoneNumber());
            teacher.setUserRole(teacherDetails.getUserRole());
            teacher.setEventer(teacherDetails.getEventer());
            return teacherRepository.save(teacher);
        }).orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}

