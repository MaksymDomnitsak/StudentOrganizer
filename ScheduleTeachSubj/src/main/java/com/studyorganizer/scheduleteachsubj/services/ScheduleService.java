package com.studyorganizer.scheduleteachsubj.services;

import com.studmodel.Group;
import com.studmodel.Schedule;
import com.studmodel.Subject;
import com.studyorganizer.scheduleteachsubj.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.getAllScheduleSorted();
    }


    public Page<Schedule> getAll(Pageable pageable)
    {
        return scheduleRepository.findPageOfSchedule(pageable);
    }

    public Page<Schedule> getScheduleByTeacherId(Pageable pageable,Long teacherId)
    {
        return scheduleRepository.findPageOfScheduleByTeacherId(pageable,teacherId);
    }

    public List<Schedule> getByGroupId(String groupId){
        return scheduleRepository.findAllByGroupIdOrdered(Long.parseLong(groupId));
    }


    public List<Schedule> getByTeacherId(Long teacherId){
        return scheduleRepository.findAllByTeacherIdOrderByDayOfWeekLessonOrder(teacherId);
    }

    public void deleteAllSchedule(){
        scheduleRepository.deleteAll();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> batchCreateSchedule(List<Schedule> schedules) {

        return scheduleRepository.saveAll(schedules);
    }

    public Schedule updateSchedule(Long id, Schedule scheduleDetails) {
        return scheduleRepository.findById(id).map(schedule -> {
            schedule.setSubject(scheduleDetails.getSubject());
            schedule.setTeacher(scheduleDetails.getTeacher());
            schedule.setGroup(scheduleDetails.getGroup());
            schedule.setDayOfWeek(scheduleDetails.getDayOfWeek());
            schedule.setEvenWeek(scheduleDetails.getEvenWeek());
            schedule.setLessonOrder(scheduleDetails.getLessonOrder());
            schedule.setTypeOfLesson(scheduleDetails.getTypeOfLesson());
            schedule.setOnline(scheduleDetails.getOnline());
            schedule.setAuditoryNumber(scheduleDetails.getAuditoryNumber());
            return scheduleRepository.save(schedule);
        }).orElseThrow(() -> new RuntimeException("Schedule not found with id " + id));
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<Subject> getSubjectsFromScheduleByTeacherId(Long teacherId){
        return scheduleRepository.findSubjectsfromScheduleByTeacher(teacherId);
    }

    public List<Group> getGroupsFromScheduleByTeacherId(Long teacherId){
        return scheduleRepository.findGroupsfromScheduleByTeacher(teacherId);
    }
}

