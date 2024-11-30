package com.studyorganizer.studgroups.services;

import com.studmodel.Student;
import com.studyorganizer.studgroups.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Long getGroupIdByStudentEmail(String email) {
        Student st = studentRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
        return st.getGroup().getId();
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        return studentRepository.findById(id).map(student -> {
            student.setEmail(studentDetails.getEmail());
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setPatronymicName(studentDetails.getPatronymicName());
            student.setPhoneNumber(studentDetails.getPhoneNumber());
            student.setUserRole(studentDetails.getUserRole());
            student.setEventer(studentDetails.getEventer());
            student.setGroup(studentDetails.getGroup());
            return studentRepository.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
