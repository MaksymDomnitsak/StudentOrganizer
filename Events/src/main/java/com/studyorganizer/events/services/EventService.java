package com.studyorganizer.events.services;

import com.studmodel.Event;
import com.studmodel.Subject;
import com.studmodel.User;
import com.studyorganizer.events.dto.EventDtoRequest;
import com.studyorganizer.events.repositories.EventsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventsRepository eventRepository;

    @Autowired
    public EventService(EventsRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return Optional.ofNullable(event);
    }

    public List<Event> getEventsByCreatorId(Long id) {
        return eventRepository.findAllByCreatorId(id);
    }

    public Event createEvent(EventDtoRequest event) {
        Event newEvent = new Event();
        if(event.getSubject() != 0) {
            Subject subject = new Subject();
            subject.setId(event.getSubject());
            newEvent.setSubject(subject);
        }
        User creator = new User();
        creator.setId(event.getCreator());
        List<User> users = new ArrayList<>();
        for(Long id: event.getAttendees()){
            User user = new User();
            user.setId(id);
            users.add(user);
        }
        newEvent.setCreator(creator);
        newEvent.setAttendees(users);
        newEvent.setAuditoryNumber(event.getAuditoryNumber());
        newEvent.setOnline(event.getIsOnline());
        newEvent.setTitle(event.getTitle());
        newEvent.setStartTime(event.getStartTime());
        newEvent.setEndTime(event.getEndTime());
        return eventRepository.save(newEvent);
    }

    public Event updateEvent(Long id, EventDtoRequest eventDetails) {
        Subject subject = new Subject();
        subject.setId(eventDetails.getSubject());
        User creator = new User();
        creator.setId(eventDetails.getCreator());
        List<User> users = new ArrayList<>();
        for(Long idx: eventDetails.getAttendees()){
            User user = new User();
            user.setId(idx);
            users.add(user);
        }
        return eventRepository.findById(id).map(event -> {
            event.setTitle(eventDetails.getTitle());
            event.setStartTime(eventDetails.getStartTime());
            event.setEndTime(eventDetails.getEndTime());
            if(subject.getId() != 0) event.setSubject(subject);
            event.setCreator(creator);
            event.setAttendees(users);
            event.setOnline(eventDetails.getIsOnline());
            event.setAuditoryNumber(eventDetails.getAuditoryNumber());
            return eventRepository.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> getAllByCreatorId(Long creatorId) {
        return eventRepository.findAllByCreatorId(creatorId);
    }

    public List<Event> getByAttendeeId(Long attendeeId) {
        return eventRepository.findAllByAttendeesId(attendeeId);
    }

    public List<Event> getByAttendeeEmail(String email) {
        return eventRepository.findAllByAttendeesEmail(email);
    }


}

