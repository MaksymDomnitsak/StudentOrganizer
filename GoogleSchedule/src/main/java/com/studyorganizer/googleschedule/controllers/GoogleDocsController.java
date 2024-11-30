package com.studyorganizer.googleschedule.controllers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.*;
import com.studmodel.Schedule;
import com.studyorganizer.googleschedule.dto.ReportDto;
import com.studyorganizer.googleschedule.security.AuthService;
import com.studyorganizer.googleschedule.services.GoogleDocsService;
import com.studyorganizer.googleschedule.services.ScheduleConsumerService;
import com.studyorganizer.googleschedule.services.ScheduleProducerService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/googleDocs")
public class GoogleDocsController {

    @Autowired
    ScheduleConsumerService scheduleConsumerService;

    @Autowired
    ScheduleProducerService scheduleProducerService;

    @Autowired
    GoogleDocsService googleDocsService;

    @PostMapping("/report")
    public ResponseEntity<String> makeReport(HttpServletRequest request, @RequestBody ReportDto report) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String resp = restTemplate.getForObject("http://fmi-schedule.chnu.edu.ua/public/semesters", String.class);
        JSONArray semesters = new JSONArray(resp);
        JSONObject semester = semesters.getJSONObject(0);
        String startDay = semester.getString("startDay");
        int week = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(startDay, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int semesterDay = LocalDate.now().getDayOfYear()-localDate.getDayOfYear()+1;
        if(localDate.getDayOfWeek().getValue() != 1){
            semesterDay+=localDate.getDayOfWeek().getValue()-1;

        }
        week += (semesterDay - 1) / 7;
        boolean isEven = week % 2 == 0;
        LocalDate weekNow = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()-1);
        CompletableFuture<List<Schedule>> list = CompletableFuture.supplyAsync(() -> {
            scheduleProducerService.requestScheduleByTeacherId(report.getTeacherId().toString(), Boolean.toString(isEven));

            List<Schedule> schedules;
            while ((schedules = scheduleConsumerService.getScheduleFromCache(report.getTeacherId().toString())) == null) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
            return schedules;
        });

        Map<String, List<Schedule>> groupedMap = list.get().stream().collect(Collectors.groupingBy(schedule -> schedule.getDayOfWeek() + "-" + schedule.getLessonOrder()));

        List<List<Schedule>> result = new ArrayList<>(groupedMap.values());

        List<List<String>> reports = new ArrayList<>();
        List<String> fullName = List.of(result.get(0).get(0).getTeacher().getLastName(),
                result.get(0).get(0).getTeacher().getFirstName(),
                result.get(0).get(0).getTeacher().getPatronymicName());
        for(List<Schedule> schedules : result) {
            List<String> strings = new ArrayList<>();
            if(week == 1 && localDate.getDayOfWeek().getValue() != 1 && schedules.get(0).getDayOfWeek() < localDate.getDayOfWeek().getValue())
                continue;
            strings.add(weekNow.plusDays(schedules.get(0).getDayOfWeek()-1).format(formatter));
            strings.add(googleDocsService.getLessonTime(schedules.get(0).getLessonOrder().intValue()));
            List<String> groups = schedules.stream().map(schedule -> schedule.getGroup().getName()).toList();
            String res;
            if(groups.size()>1){
                res = String.join(",", groups)+" групи";
            }else{
                res = groups.get(0)+" група";
            }
            strings.add(schedules.get(0).getSubject().getName()+", "+googleDocsService.getLessonType(schedules.get(0).getTypeOfLesson().toString())+". "+res);
            if(report.getSetOnline()){
                if(schedules.get(0).getOnline()) strings.add("Онлайн. Посилання: ...");
                else strings.add(schedules.get(0).getAuditoryNumber());
            }
            reports.add(strings);
        }

        Docs service = new Docs.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), AuthService.makeCredentials(request))
                .setApplicationName("StudentOrganizer")
                .build();

        Document document = new Document().setTitle("Звіт за поточний тиждень");
        Document createdDoc = service.documents().create(document).execute();

        List<Request> requests = new ArrayList<>();

        List<String> start = new ArrayList<>(List.of("Звіт\n", "про проведення аудиторних занять\n",
                String.join(" ", fullName) + "\n", "(прізвище, ім’я, по батькові)\n", "з " + weekNow.format(formatter)
                        + " по " + weekNow.plusDays(6).format(formatter) + "\n\n"));
        int index = 1;

        requests.addAll(googleDocsService.createTextRequest(start.get(0), index, true, 18, false, false, "CENTER"));
        index += start.get(0).length();
        requests.addAll(googleDocsService.createTextRequest(start.get(1), index, false, 14, false, false, "CENTER"));
        index += start.get(1).length();

        requests.addAll(googleDocsService.createTextRequest(start.get(2), index, false, 12, true, true, "CENTER"));
        index += start.get(2).length();
        requests.addAll(googleDocsService.createTextRequest(start.get(3), index, false, 10, false, false, "CENTER"));
        index += start.get(3).length();

        requests.addAll(googleDocsService.createTextRequest(start.get(4), index, false, 12, false, false, "CENTER"));
        index += start.get(4).length();

        requests.addAll(googleDocsService.createNumberedList(reports, index, report.getSetOnline(), report.getSetTopic()));

        index += googleDocsService.calculateTotalLength(reports, report.getSetOnline(), report.getSetTopic());
        String extra = "\nВикладач\t\t\t\t\t\t\t\t" + fullName.get(1) + " " + fullName.get(0) + "\n";
        requests.addAll(googleDocsService.createTextRequest(extra, index, false, 12, false, false, "START"));
        index += extra.length();
        requests.addAll(googleDocsService.createTextRequest("Завідувач кафедри\t\t\t\t\t\t\t\tЯрослав Бігун\n", index, false, 12, false, false, "START"));

        BatchUpdateDocumentRequest batchRequest = new BatchUpdateDocumentRequest().setRequests(requests);
        service.documents().batchUpdate(createdDoc.getDocumentId(), batchRequest).execute();

        return ResponseEntity.ok(createdDoc.getDocumentId());
    }


}
