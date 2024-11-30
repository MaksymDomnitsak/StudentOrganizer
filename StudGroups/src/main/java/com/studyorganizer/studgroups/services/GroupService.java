package com.studyorganizer.studgroups.services;

import com.studmodel.Group;
import com.studyorganizer.studgroups.dto.GroupDto;
import com.studyorganizer.studgroups.dto.SemesterResponse;
import com.studyorganizer.studgroups.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> getEnabledGroups() {
        return groupRepository.findGroupsByDisabled(false);
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Group getGroupByName(String name) {
        return groupRepository.findGroupByName(name);
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group createGroup(SemesterResponse.Group groupDto) {
        Group group = new Group();
        group.setName(groupDto.getTitle());
        group.setDisabled(groupDto.getDisable());
        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, Group groupDetails) {
        return groupRepository.findById(id).map(group -> {
            group.setName(groupDetails.getName());
            group.setStudents(groupDetails.getStudents());
            group.setDisabled(groupDetails.isDisabled());
            return groupRepository.save(group);
        }).orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}