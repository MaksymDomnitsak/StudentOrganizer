package com.studyorganizer.googleschedule.controllers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.studyorganizer.googleschedule.dto.EventDto;
import com.studyorganizer.googleschedule.dto.ScheduleDto;
import com.studyorganizer.googleschedule.extras.RandomRequestIdGenerator;
import com.studyorganizer.googleschedule.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value="/api/calendar")
@Slf4j
public class GoogleCalendarController {

    @PostMapping("/createSchedule")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> importSchedule(HttpServletRequest request, @RequestBody ScheduleDto scheduleForm) throws IOException, GeneralSecurityException {
        Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), AuthService.makeCredentials(request))
                .setApplicationName("StudentOrganizer")
                .build();
        Event event = new Event();
        event.setSummary(scheduleForm.getSummary());
        event.setLocation(scheduleForm.getLocation());
        event.setDescription(scheduleForm.getDescription());

        LocalTime start = LocalTime.parse(scheduleForm.getStartTime());
        LocalTime end = LocalTime.parse(scheduleForm.getEndTime());
        DateTime startDateTime =new DateTime(scheduleForm.getStartDate()+"T"+start.minusHours(2).toString()+":00");
        DateTime endDateTime = new DateTime(scheduleForm.getStartDate()+"T"+end.minusHours(2).toString()+":00");
        event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
        event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev"));

        if (scheduleForm.getFrequency().equals("weekly")) {
            event.setRecurrence(Collections.singletonList("RRULE:FREQ=WEEKLY;COUNT=" + scheduleForm.getRepeats()));
        } else if (scheduleForm.getFrequency().equals("two-weeks")) {
            event.setRecurrence(Collections.singletonList("RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=" + scheduleForm.getRepeats()));
        }
        List<EventAttendee> allAttendees = new ArrayList<>();

        for(String email: scheduleForm.getAttendeesEmails()){
            allAttendees.add(new EventAttendee().setEmail(email));
        }
        event.setAttendees(allAttendees);
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        if(!scheduleForm.getConference().equals("none")) {
            ConferenceData conferenceData = new ConferenceData();
            ConferenceSolutionKey conferenceSolution = new ConferenceSolutionKey();
            conferenceSolution.setType("hangoutsMeet");
            CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
            createConferenceRequest.setRequestId(RandomRequestIdGenerator.generateRandomRequestId());
            createConferenceRequest.setConferenceSolutionKey(conferenceSolution);
            conferenceData.setCreateRequest(createConferenceRequest);
            event.setConferenceData(conferenceData);
        }
        service.events().insert("primary", event).setConferenceDataVersion(1).execute();
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/createEvent")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> importEvent(HttpServletRequest request, @RequestBody EventDto eventForm) throws IOException, GeneralSecurityException {
        Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), AuthService.makeCredentials(request))
                .setApplicationName("StudentOrganizer")
                .build();
        Event event = new Event();
        event.setLocation(eventForm.getLocation());
        event.setSummary(eventForm.getSummary());
        event.setDescription(eventForm.getDescription());

        DateTime startDateTime =new DateTime(eventForm.getStartDateTime().minusHours(2).toString()+":00");
        DateTime endDateTime = new DateTime(eventForm.getEndDateTime().minusHours(2).toString()+":00");
        event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
        event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev"));

        event = googleCalendarService(event,eventForm.getFrequency(),eventForm.getRepeats(),eventForm.getAttendeesEmails(), eventForm.getConference());
        service.events().insert("primary", event).setConferenceDataVersion(1).execute();
        return ResponseEntity.ok("Ok");
    }

    private Event googleCalendarService(Event event,String frequency, String repeats, String[] attendeesEmails, String conference){
        if (frequency.equals("weekly")) {
            event.setRecurrence(Collections.singletonList("RRULE:FREQ=WEEKLY;COUNT=" + repeats));
        } else if (frequency.equals("two-weeks")) {
            event.setRecurrence(Collections.singletonList("RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=" + repeats));
        }
        List<EventAttendee> allAttendees = new ArrayList<>();

        for(String email: attendeesEmails){
            allAttendees.add(new EventAttendee().setEmail(email));
        }
        event.setAttendees(allAttendees);
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(12 * 60),
                new EventReminder().setMethod("popup").setMinutes(15),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        if(!conference.equals("none")) {
            ConferenceData conferenceData = new ConferenceData();
            ConferenceSolutionKey conferenceSolution = new ConferenceSolutionKey();
            conferenceSolution.setType("hangoutsMeet");
            CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
            createConferenceRequest.setRequestId(RandomRequestIdGenerator.generateRandomRequestId());
            createConferenceRequest.setConferenceSolutionKey(conferenceSolution);
            conferenceData.setCreateRequest(createConferenceRequest);
            event.setConferenceData(conferenceData);
        }

        return event;
    }
}
