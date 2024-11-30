package com.studyorganizer.scheduleteachsubj.controllers;
import com.studmodel.Teacher;
import com.studyorganizer.scheduleteachsubj.dto.TeacherDTO;
import com.studyorganizer.scheduleteachsubj.dto.TeacherDtoResponse;
import com.studyorganizer.scheduleteachsubj.mappers.TeacherToTeacherDtoMapper;
import com.studyorganizer.scheduleteachsubj.services.TeacherService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherToTeacherDtoMapper mapper = Mappers.getMapper(TeacherToTeacherDtoMapper.class);

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TeacherDtoResponse>> getAll() {
        List<TeacherDtoResponse> teacherList = new ArrayList<>();
        teacherService.getAllTeachers().forEach(teacher -> teacherList.add(mapper.teacherToTeacherDto(teacher)));
        return ResponseEntity.ok(teacherList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherService.createTeacher(teacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, teacherDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/load-teachers")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> loadTeachers() {
        String url = "http://fmi-schedule.chnu.edu.ua/public/teachers";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TeacherDTO[]> response = restTemplate.getForEntity(url, TeacherDTO[].class);

        if (response.getBody() != null) {
            List<TeacherDTO> teachers = List.of(response.getBody());
            teachers.forEach(teacherService::createTeacher);

            return ResponseEntity.ok("Teachers loaded and saved successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to load teachers.");
        }
    }
}
