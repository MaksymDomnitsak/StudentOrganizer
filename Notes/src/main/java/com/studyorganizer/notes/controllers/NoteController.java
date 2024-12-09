package com.studyorganizer.notes.controllers;

import com.studmodel.Note;
import com.studmodel.Schedule;
import com.studmodel.Student;
import com.studyorganizer.notes.dto.GroupedNote;
import com.studyorganizer.notes.dto.NoteDtoRequest;
import com.studyorganizer.notes.dto.NoteDtoResponse;
import com.studyorganizer.notes.mappers.NoteMapper;
import com.studyorganizer.notes.services.NoteService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    NoteMapper mapper = Mappers.getMapper(NoteMapper.class);

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping(params = {"studentId"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NoteDtoResponse>> getNotesByStudentId(@RequestParam("studentId") Long studentId){
        List<NoteDtoResponse> noteList = new ArrayList<>();
        noteService.getByStudentId(studentId).forEach(note -> noteList.add(mapper.noteToNoteDto(note)));
        return ResponseEntity.ok(noteList);
    }


    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<NoteDtoResponse>> getAll() {
        List<NoteDtoResponse> noteList = new ArrayList<>();
        noteService.getAllNotes().forEach(note -> noteList.add(mapper.noteToNoteDto(note)));
        return ResponseEntity.ok(noteList);
    }

    @GetMapping(params = {"page","size"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<NoteDtoResponse>> getPaginated(@RequestParam("page") int page , @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page,size);
        List<NoteDtoResponse> noteList = new ArrayList<>();
        noteService.getAll(pageable).forEach(note -> noteList.add(mapper.noteToNoteDto(note)));
        return ResponseEntity.ok(noteList);
    }


    @GetMapping(params = {"isFinished"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NoteDtoResponse>> getNotesByFinishedIs(@RequestParam("isFinished") Boolean isFinished) {
        List<NoteDtoResponse> noteList = new ArrayList<>();
        noteService.getByFinishedIs(isFinished).forEach(note -> noteList.add(mapper.noteToNoteDto(note)));
        return ResponseEntity.ok(noteList);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN') or hasAnyAuthority('STUDENT')")
    public ResponseEntity<NoteDtoResponse> getNoteById(@PathVariable("id") Long id){
        NoteDtoResponse response = mapper.noteToNoteDto(noteService.getNoteById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NoteDtoResponse> createNote(@RequestBody NoteDtoRequest note){
        return ResponseEntity.ok(mapper.noteToNoteDto(noteService.createNote(mapper.noteDtoToNote(note))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNote(@PathVariable("id") Long id)
    {
        noteService.deleteNote(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NoteDtoResponse> updateNote(@PathVariable("id") Long id,
                                                      @RequestBody NoteDtoRequest note){
        return ResponseEntity.ok(mapper.noteToNoteDto(noteService.updateNote(id, mapper.noteDtoToNote(note))));
    }

    @PostMapping("/groupCreate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NoteDtoResponse>> createGroupedNote(@RequestBody GroupedNote note){
        List<NoteDtoResponse> noteList = new ArrayList<>();
        List<Note> notes = new ArrayList<>();


        Arrays.stream(note.getStudents()).toList().forEach(id -> {
            Note newNote = new Note();
            newNote.setTitle(note.getTitle());
            newNote.setBody(note.getBody());
            Schedule schedule = new Schedule();
            schedule.setId(note.getSchedule());
            newNote.setLesson(schedule);
            newNote.setFinished(false);
            Student student = new Student();
            student.setId(id);
            newNote.setStudent(student);
            notes.add(newNote);
        });
        noteService.createListOfNotes(notes);
        return ResponseEntity.ok(noteList);
    }

}

