package com.studyorganizer.scheduleteachsubj.extras;

import com.studmodel.*;
import com.studyorganizer.scheduleteachsubj.services.SubjectService;
import com.studyorganizer.scheduleteachsubj.services.TeacherService;
import org.json.JSONObject;

public class ScheduleUtils {

    public static Long mapDayToNumber(String day) {
        return switch (day.toUpperCase()) {
            case "MONDAY" -> 1L;
            case "TUESDAY" -> 2L;
            case "WEDNESDAY" -> 3L;
            case "THURSDAY" -> 4L;
            case "FRIDAY" -> 5L;
            default -> throw new IllegalArgumentException("Unknown day: " + day);
        };
    }

    public static Schedule processWeek(JSONObject weekData, Boolean isEven, Group group, Long dayNumber, Long lessonOrder, TeacherService teacherService, SubjectService subjectService) {
        if (weekData == null) return null;

        String subjectName = weekData.getString("subjectForSite");
        Subject subject = subjectService.createSubject(new Subject(subjectName));
        LessonType lessonType = LessonType.valueOf(weekData.getString("lessonType"));
        JSONObject jsonTeacher = weekData.getJSONObject("teacher");
        Teacher teacher = teacherService.getTeacherByFullName(jsonTeacher.getString("name"),
                jsonTeacher.getString("surname"),jsonTeacher.getString("patronymic"));

        JSONObject room = weekData.getJSONObject("room");
        String roomName = room.getString("name");
        boolean isOnline = roomName.contains("онлайн");

        // Додаємо до результату
        Schedule schedule = new Schedule();
        schedule.setSubject(subject);
        schedule.setDayOfWeek(dayNumber);
        schedule.setLessonOrder(lessonOrder);
        schedule.setTypeOfLesson(lessonType);
        schedule.setTeacher(teacher);
        schedule.setEvenWeek(isEven);
        schedule.setGroup(group);
        if (isOnline) {
            schedule.setOnline(true);
            schedule.setAuditoryNumber(null);
        } else {
            schedule.setOnline(false);
            schedule.setAuditoryNumber(roomName);
        }
        return schedule;
    }
}
