package com.studyorganizer.studgroups.controllers;

import com.studmodel.Student;
import com.studyorganizer.studgroups.dto.StudentDtoRequest;
import com.studyorganizer.studgroups.dto.StudentDtoResponse;
import com.studyorganizer.studgroups.mappers.StudentMapper;
import com.studyorganizer.studgroups.services.StudentService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    StudentMapper mapper = Mappers.getMapper(StudentMapper.class);

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(value ="/byEmail", params={"email"})
    public Long getGroupIdByStudentEmail(@RequestParam("email")String email){
        return studentService.getGroupIdByStudentEmail(email);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StudentDtoResponse>> getAll() {
        List<StudentDtoResponse> studentList = new ArrayList<>();
        studentService.getAll().forEach(student -> studentList.add(mapper.studentToStudentDto(student)));
        return ResponseEntity.ok(studentList);
    }

    @GetMapping(params = {"page","size"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StudentDtoResponse>> getPaginated(@RequestParam("page") int page , @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page,size);
        List<StudentDtoResponse> studentList = new ArrayList<>();
        studentService.getAll(pageable).forEach(student -> studentList.add(mapper.studentToStudentDto(student)));
        return ResponseEntity.ok(studentList);
    }

//    @GetMapping(params = {"group"})
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<List<StudentDtoResponse>> getAllByGroupId(@RequestParam("group") Long group){
//        List<StudentDtoResponse> studentList = new ArrayList<>();
//        studentService.getByGroup(group).forEach(student -> studentList.add(mapper.studentToStudentDto(student)));
//        return ResponseEntity.ok(studentList);
//    }
//
//    @GetMapping(params = {"group","page","size"})
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<List<StudentDtoResponse>> getPaginated(@RequestParam("group") Long group ,@RequestParam("page") int page , @RequestParam("size") int size){
//        Pageable pageable = PageRequest.of(page,size);
//        List<StudentDtoResponse> studentList = new ArrayList<>();
//        studentService.getPageByGroup(group,pageable).forEach(student -> studentList.add(mapper.studentToStudentDto(student)));
//        return ResponseEntity.ok(studentList);
//    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public StudentDtoResponse createStudent(@RequestBody StudentDtoRequest student){
        return mapper.studentToStudentDto(studentService.createStudent(mapper.studentDtoToStudent(student)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteStudent(@PathVariable("id") Long id)
    {
        studentService.delete(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public StudentDtoResponse updateStudent(@PathVariable("id") Long id,
                                            @RequestBody StudentDtoRequest student){
        return mapper.studentToStudentDto(studentService.updateStudent(id, mapper.studentDtoToStudent(student)));
    }
}
