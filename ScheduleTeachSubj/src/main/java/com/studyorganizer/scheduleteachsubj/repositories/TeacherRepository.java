package com.studyorganizer.scheduleteachsubj.repositories;

import com.studmodel.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByFirstNameAndLastNameAndAndPatronymicName(String firstName, String lastName, String patronymicName);
}
