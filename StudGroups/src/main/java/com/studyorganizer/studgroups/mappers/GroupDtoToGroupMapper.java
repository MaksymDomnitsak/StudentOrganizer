package com.studyorganizer.studgroups.mappers;

import com.studmodel.Group;
import com.studyorganizer.studgroups.dto.GroupDto;
import com.studyorganizer.studgroups.dto.GroupDtoWithStudents;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupDtoToGroupMapper {
    Group groupDtoToGroupMapper(GroupDto groupDto);

    GroupDto groupToGroupDtoMapper(Group group);

    GroupDtoWithStudents groupToGroupDtoWithStudentsMapper(Group group);

}
