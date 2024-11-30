package com.studyorganizer.scheduleteachsubj.controllers;

import com.studmodel.*;
import com.studyorganizer.scheduleteachsubj.dto.ScheduleDtoResponse;
import com.studyorganizer.scheduleteachsubj.dto.SubjectDtoResponse;
import com.studyorganizer.scheduleteachsubj.extras.ScheduleUtils;
import com.studyorganizer.scheduleteachsubj.mappers.ScheduleDtoToScheduleMapper;
import com.studyorganizer.scheduleteachsubj.services.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.studyorganizer.scheduleteachsubj.extras.ScheduleUtils.mapDayToNumber;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;


    private final SubjectService subjectService;

    private final TeacherService teacherService;

    private ScheduleDtoToScheduleMapper mapper = Mappers.getMapper(ScheduleDtoToScheduleMapper.class);

    @Autowired
    private GroupProducerService groupProducerService;

    @Autowired
    private GroupConsumerService groupConsumerService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService,SubjectService subjectService, TeacherService teacherService) {
        this.scheduleService = scheduleService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDtoResponse> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.scheduleToDtoSchedule(scheduleService.getScheduleById(id).get()));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ScheduleDtoResponse>> getAllSorted() {
        List<ScheduleDtoResponse> scheduleList = new ArrayList<>();
        scheduleService.getAllSchedules().forEach(schedule -> scheduleList.add(mapper.scheduleToDtoSchedule(schedule)));
        return ResponseEntity.ok(scheduleList);
    }

    @GetMapping(params = {"groupId"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ScheduleDtoResponse>> getAllByGroupId(@RequestParam("groupId") String groupId){
        List<ScheduleDtoResponse> scheduleList = new ArrayList<>();
        scheduleService.getByGroupId(groupId).forEach(schedule -> scheduleList.add(mapper.scheduleToDtoSchedule(schedule)));
        return ResponseEntity.ok(scheduleList);
    }
    @GetMapping(params = {"teacherId"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ScheduleDtoResponse>> getAllByTeacherId(@RequestParam("teacherId") Long teacherId){
        List<ScheduleDtoResponse> scheduleList = new ArrayList<>();
        scheduleService.getByTeacherId(teacherId)
                .forEach(schedule -> scheduleList.add(mapper.scheduleToDtoSchedule(schedule)));
        return ResponseEntity.ok(scheduleList);
    }

    @GetMapping(params = {"page","size"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<ScheduleDtoResponse>> getPaginated(@RequestParam("page") int page , @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page,size);
        List<ScheduleDtoResponse> scheduleList = new ArrayList<>();
        Page<Schedule> events = scheduleService.getAll(pageable);
        events.forEach(schedule -> scheduleList.add(mapper.scheduleToDtoSchedule(schedule)));
        Page<ScheduleDtoResponse> pageEvents = new PageImpl<>(scheduleList,pageable,events.getTotalElements());
        return ResponseEntity.ok(pageEvents);
    }

    @GetMapping(params = {"page","size","teacherId"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<ScheduleDtoResponse>> getPaginatedByTeacherId(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  @RequestParam("teacherId") Long teacherId){
        Pageable pageable = PageRequest.of(page,size);
        List<ScheduleDtoResponse> scheduleList = new ArrayList<>();
        Page<Schedule> events = scheduleService.getScheduleByTeacherId(pageable,teacherId);
        events.forEach(schedule -> scheduleList.add(mapper.scheduleToDtoSchedule(schedule)));
        Page<ScheduleDtoResponse> pageEvents = new PageImpl<>(scheduleList,pageable,events.getTotalElements());
        return ResponseEntity.ok(pageEvents);
    }

    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleService.createSchedule(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, scheduleDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/load-schedule")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> loadSchedule() throws ExecutionException, InterruptedException {
        scheduleService.deleteAllSchedule();
        RestTemplate restTemplate = new RestTemplate();

        String resp = restTemplate.getForObject("http://fmi-schedule.chnu.edu.ua/public/semesters",String.class);
        JSONArray semesters = new JSONArray(resp);
        JSONObject semester = semesters.getJSONObject(0);
        Long semesterId = semester.getLong("id");

        // Отримуємо JSON у вигляді рядка
        String response = restTemplate.getForObject("http://fmi-schedule.chnu.edu.ua/schedules/full/semester?semesterId="+ semesterId, String.class);

        // Парсимо JSON
        JSONObject jsonObject = new JSONObject(response);
        JSONArray scheduleArray = jsonObject.getJSONArray("schedule");

        // Збираємо дані
        List<Schedule> result = new ArrayList<>();

        for (int i = 0; i < scheduleArray.length(); i++) {
            JSONObject groupData = scheduleArray.getJSONObject(i);

            // Отримуємо назву групи
            Long groupId = groupData.getJSONObject("group").getLong("id");
            String groupTitle = groupData.getJSONObject("group").getString("title");
            CompletableFuture<Long> id = CompletableFuture.supplyAsync(() -> {
                groupProducerService.requestGroupIdByGroupTitle(groupId.toString(), groupTitle);

                Long groupIdfromDB;
                while ((groupIdfromDB = groupConsumerService.getGroupIdFromCache(groupId.toString())) == null) {
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
                return groupIdfromDB;
            });
            Group group = new Group(groupTitle);
            group.setId(id.get());
            JSONArray days = groupData.getJSONArray("days");

            for (int j = 0; j < days.length(); j++) {
                JSONObject dayData = days.getJSONObject(j);

                // День тижня
                Long dayNumber = mapDayToNumber(dayData.getString("day"));

                JSONArray classes = dayData.getJSONArray("classes");

                for (int k = 0; k < classes.length(); k++) {
                    JSONObject classData = classes.getJSONObject(k);

                    JSONObject weeks = classData.getJSONObject("weeks");
                    JSONObject evenWeek = weeks.optJSONObject("even");
                    JSONObject oddWeek = weeks.optJSONObject("odd");
                    JSONObject order = classData.getJSONObject("class");
                    Long lessonOrder = Long.parseLong(order.getString("class_name"));
                    Schedule even =  ScheduleUtils.processWeek(evenWeek, true, group, dayNumber, lessonOrder, teacherService, subjectService);
                    Schedule odd = ScheduleUtils.processWeek(oddWeek, false, group, dayNumber, lessonOrder, teacherService, subjectService);
                    if (even != null) result.add(even);
                    if (odd != null) result.add(odd);
                }
            }
        }
        scheduleService.batchCreateSchedule(result);
        return ResponseEntity.ok("Schedule saved successfully!");
    }

    @GetMapping(value = "/getSubjects", params = {"teacherId"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Subject>> getAll(@RequestParam("teacherId") Long teacherId) {
        return ResponseEntity.ok(scheduleService.getSubjectsFromScheduleByTeacherId(teacherId));
    }

}

