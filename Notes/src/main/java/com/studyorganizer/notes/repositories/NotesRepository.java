package com.studyorganizer.notes.repositories;

import com.studmodel.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByIsFinished(Boolean isFinished);

    @Query("SELECT ls FROM Note ls WHERE TYPE(ls.student) = Student AND ls.student.id = :studentId ORDER BY ls.createdAt ASC")
    List<Note> findAllByStudentId(Long studentId);
}
