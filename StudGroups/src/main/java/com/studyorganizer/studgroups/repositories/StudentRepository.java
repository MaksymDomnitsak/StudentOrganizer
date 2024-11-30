package com.studyorganizer.studgroups.repositories;

import com.studmodel.Student;
import com.studmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    Optional<Student> findByEmail(String email);
}
