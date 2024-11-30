package com.studyorganizer.events.mappers;

import com.studmodel.Event;
import com.studyorganizer.events.dto.EventDtoRequest;
import com.studyorganizer.events.dto.EventDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMappers {
    EventDtoResponse eventToEventDto(Event event);

    //Event dtoEventToEvent(EventDtoRequest dtoEvent);
}
