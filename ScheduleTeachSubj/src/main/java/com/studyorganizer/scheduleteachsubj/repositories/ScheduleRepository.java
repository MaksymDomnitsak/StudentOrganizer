package com.studyorganizer.scheduleteachsubj.repositories;

import com.studmodel.Group;
import com.studmodel.Schedule;
import com.studmodel.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "select ls from Schedule ls where ls.teacher.id = :teacherId order by ls.dayOfWeek ASC, ls.isEvenWeek ASC, ls.lessonOrder ASC, ls.group.id ASC")
    List<Schedule> findAllByTeacherIdOrderByDayOfWeekLessonOrder(Long teacherId);

    @Query(value = "select ls from Schedule ls where ls.group.id = :groupId order by ls.dayOfWeek ASC, ls.isEvenWeek ASC, ls.lessonOrder ASC ")
    List<Schedule> findAllByGroupIdOrdered(Long groupId);

    @Query(value = "select ls from Schedule ls order by ls.dayOfWeek ASC,ls.lessonOrder ASC, ls.isEvenWeek ASC, ls.group.id ASC")
    List<Schedule> getAllScheduleSorted();

    @Query("select ls from Schedule ls order by ls.dayOfWeek ASC,ls.lessonOrder ASC, ls.isEvenWeek ASC, ls.group.id ASC")
    Page<Schedule> findPageOfSchedule(Pageable pageable);

    @Query("select ls from Schedule ls where ls.teacher.id = :teacherId order by ls.dayOfWeek ASC,ls.lessonOrder ASC, ls.isEvenWeek ASC, ls.group.id ASC")
    Page<Schedule> findPageOfScheduleByTeacherId(Pageable pageable,Long teacherId);

    @Query("select ls from Schedule ls where ls.teacher.id = :teacherId and ls.isEvenWeek = :evenWeek order by ls.dayOfWeek ASC,ls.lessonOrder ASC")
    List<Schedule> findAllByTeacherIdAndEvenWeek(Long teacherId, Boolean evenWeek);

    @Query(value = "select distinct ls.group from Schedule ls where ls.teacher.id = :creatorId")
    List<Group> findGroupsfromScheduleByTeacher(Long creatorId);

    @Query(value = "select ls.subject from Schedule ls where ls.teacher.id = :creatorId")
    List<Subject> findSubjectsfromScheduleByTeacher(Long creatorId);


}
