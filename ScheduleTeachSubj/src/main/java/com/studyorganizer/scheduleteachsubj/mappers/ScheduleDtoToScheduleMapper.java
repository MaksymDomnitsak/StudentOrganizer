package com.studyorganizer.scheduleteachsubj.mappers;

import com.studmodel.Schedule;
import com.studyorganizer.scheduleteachsubj.dto.ScheduleDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleDtoToScheduleMapper {

    ScheduleDtoResponse scheduleToDtoSchedule(Schedule schedule);

}
