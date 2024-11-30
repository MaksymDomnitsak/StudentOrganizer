package com.studyorganizer.studgroups.controllers;

import com.studmodel.Group;
import com.studyorganizer.studgroups.dto.GroupDto;
import com.studyorganizer.studgroups.dto.GroupDtoWithStudents;
import com.studyorganizer.studgroups.dto.SemesterResponse;
import com.studyorganizer.studgroups.mappers.GroupDtoToGroupMapper;
import com.studyorganizer.studgroups.services.GroupService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    private GroupDtoToGroupMapper mapper = Mappers.getMapper(GroupDtoToGroupMapper.class);

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GroupDto>> getAll(){
        List<GroupDto> groupList = new ArrayList<>();
        groupService.getAllGroups().forEach(group -> groupList.add(mapper.groupToGroupDtoMapper(group)));
        return ResponseEntity.ok(groupList);
    }

    @GetMapping("/enabled")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GroupDto>> getEnabled() {
        List<GroupDto> groupList = new ArrayList<>();
        groupService.getEnabledGroups().forEach(group -> groupList.add(mapper.groupToGroupDtoMapper(group)));
        return ResponseEntity.ok(groupList);
    }

    @GetMapping("/withStudents")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GroupDtoWithStudents>> getGroupsWithStudents() {
        List<GroupDtoWithStudents> groupList = new ArrayList<>();
        groupService.getEnabledGroups().forEach(group -> groupList.add(mapper.groupToGroupDtoWithStudentsMapper(group)));
        return ResponseEntity.ok(groupList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group groupDetails) {
        return ResponseEntity.ok(groupService.updateGroup(id, groupDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/load-groups")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> loadGroups() {
        String url = "http://fmi-schedule.chnu.edu.ua/semesters/default";

        RestTemplate restTemplate = new RestTemplate();
        SemesterResponse response = restTemplate.getForObject(url, SemesterResponse.class);

        response.getSemester_groups().forEach(groupService::createGroup);

        return ResponseEntity.ok("Teachers loaded and saved successfully.");
    }
}
