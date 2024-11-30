package com.studyorganizer.scheduleteachsubj.services;

import com.studmodel.Subject;
import com.studyorganizer.scheduleteachsubj.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    public Subject createSubject(Subject subject) {
        Subject newSubject = subjectRepository.findByName(subject.getName());
        return newSubject == null ? subjectRepository.save(subject) : newSubject;
    }

    public Subject updateSubject(Long id, Subject subjectDetails) {
        return subjectRepository.findById(id).map(subject -> {
            subject.setName(subjectDetails.getName());
            return subjectRepository.save(subject);
        }).orElseThrow(() -> new RuntimeException("Subject not found with id " + id));
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
