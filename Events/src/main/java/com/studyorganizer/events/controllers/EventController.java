package com.studyorganizer.events.controllers;

import com.studmodel.Event;
import com.studmodel.User;
import com.studyorganizer.events.dto.EventDtoRequest;
import com.studyorganizer.events.dto.EventDtoResponse;
import com.studyorganizer.events.mappers.EventMappers;
import com.studyorganizer.events.services.EventConsumerService;
import com.studyorganizer.events.services.EventProducerService;
import com.studyorganizer.events.services.EventService;
import jakarta.transaction.Transactional;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    private EventMappers eventMapper = Mappers.getMapper(EventMappers.class);

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    private EventProducerService eventProducerService;

    @Autowired
    private EventConsumerService eventConsumerService;

    @GetMapping("/byCreator/{creatorId}")
    @Transactional
    public CompletableFuture<List<Event>> getEventsByCreatorId(@PathVariable Long creatorId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Event> events = eventService.getEventsByCreatorId(creatorId);

            List<CompletableFuture<Void>> futures = events.stream()
                    .map(event -> CompletableFuture.runAsync(() -> {
                        List<Long> attendeeIds = event.getAttendees().stream().map(User::getId).toList();
                        eventProducerService.requestUsersByEventId(event.getId().toString(), attendeeIds);

                        List<User> attendees;
                        while ((attendees = eventConsumerService.getUsersFromCache(event.getId())) == null) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                throw new IllegalStateException(e);
                            }
                        }
                        event.setAttendees(attendees);
                    }))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            return events;
        });
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('TRUE')")
    public ResponseEntity<EventDtoResponse> createEvent(@RequestBody EventDtoRequest eventDtoRequest) {
        System.out.println(eventDtoRequest.getEndTime());
        EventDtoResponse createdEvent =  eventMapper.eventToEventDto(eventService.createEvent(eventDtoRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('TRUE')")
    public ResponseEntity<EventDtoResponse> updateEvent(@PathVariable Long id, @RequestBody EventDtoRequest eventDtoRequest) {
        EventDtoResponse updatedEvent = eventMapper.eventToEventDto(eventService.updateEvent(id, eventDtoRequest));
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('TRUE')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDtoResponse> getById(@PathVariable Long id) {
        EventDtoResponse event =  eventMapper.eventToEventDto(eventService.getEventById(id).get());
        return ResponseEntity.ok(event);
    }

    @GetMapping(value = "/creator", params = {"creatorId"})
    public ResponseEntity<List<EventDtoResponse>> getAllByCreatorId(@RequestParam("creatorId") Long creatorId) {
        List<EventDtoResponse> events = new ArrayList<>();
                eventService.getAllByCreatorId(creatorId).forEach(event -> events.add(eventMapper.eventToEventDto(event)));
        return ResponseEntity.ok(events);
    }

    @GetMapping(value = "/attendee", params = {"attendeeId"})
    public ResponseEntity<List<EventDtoResponse>> getByAttendeeId(@RequestParam("attendeeId") Long attendeeId) {
        List<EventDtoResponse> events = new ArrayList<>();
                eventService.getByAttendeeId(attendeeId).forEach(event -> events.add(eventMapper.eventToEventDto(event)));
        return ResponseEntity.ok(events);
    }
}

