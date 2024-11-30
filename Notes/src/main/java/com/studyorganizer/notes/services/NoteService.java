package com.studyorganizer.notes.services;

import com.studmodel.Note;
import com.studyorganizer.notes.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note noteDetails) {
        return noteRepository.findById(id).map(note -> {
            note.setTitle(noteDetails.getTitle());
            note.setLesson(noteDetails.getLesson());
            note.setStudent(noteDetails.getStudent());
            note.setBody(noteDetails.getBody());
            note.setFinished(noteDetails.getFinished());
            return noteRepository.save(note);
        }).orElseThrow(() -> new RuntimeException("Note not found with id " + id));
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}

