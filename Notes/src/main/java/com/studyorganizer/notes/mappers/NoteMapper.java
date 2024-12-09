package com.studyorganizer.notes.mappers;

import com.studmodel.Note;
import com.studyorganizer.notes.dto.NoteDtoRequest;
import com.studyorganizer.notes.dto.NoteDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    Note noteDtoToNote(NoteDtoRequest noteDto);

    NoteDtoResponse noteToNoteDto(Note note);
}
