package com.studyorganizer.events.repositories;

import com.studmodel.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Event,Long> {

    List<Event> findAllByCreatorId(Long creatorId);

    @Query("SELECT e FROM Event e JOIN e.attendees a WHERE a.id = :attendeeId")
    List<Event> findAllByAttendeesId(@Param("attendeeId") Long attendeeId);

    @Query("SELECT e FROM Event e JOIN e.attendees a WHERE a.email = :email")
    List<Event> findAllByAttendeesEmail(@Param("email") String email);
}
