package com.studyorganizer.userstokens.mappers;

import com.studmodel.User;
import com.studyorganizer.userstokens.dto.UserDtoRequest;
import com.studyorganizer.userstokens.dto.UserDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserToUserDtoMapper {
    User userDtoToUser(UserDtoRequest userDto);

    UserDtoResponse userToDtoUser(User user);

}
