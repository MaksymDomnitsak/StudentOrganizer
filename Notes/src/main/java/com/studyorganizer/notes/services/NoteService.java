package com.studyorganizer.notes.services;

import com.studmodel.Note;
import com.studyorganizer.notes.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NotesRepository noteRepository;

    @Autowired
    public NoteService(NotesRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).get();
    }

    public Note createNote(Note note) {
        note.setFinished(false);
        return noteRepository.save(note);
    }

    public List<Note> createListOfNotes(List<Note> notes) {
        return noteRepository.saveAll(notes);
    }

    public Page<Note> getAll(Pageable pageable)
    {
        Page<Note> page = noteRepository.findAll(pageable);
        return page;
    }

    public Note updateNote(Long id, Note noteDetails) {
        return noteRepository.findById(id).map(note -> {
            note.setTitle(noteDetails.getTitle());
            note.setLesson(note.getLesson());
            note.setStudent(note.getStudent());
            note.setBody(noteDetails.getBody());
            note.setFinished(noteDetails.getFinished());
            return noteRepository.save(note);
        }).orElseThrow(() -> new RuntimeException("Note not found with id " + id));
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public List<Note> getByStudentId(Long studentId){
        return noteRepository.findAllByStudentId(studentId);
    }

    public List<Note> getByFinishedIs(boolean isFinished){
        return noteRepository.findAllByIsFinished(isFinished);
    }
}

