package com.cpierres.p3backspring.mappers;

import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.model.RegisterRequest;
import com.cpierres.p3backspring.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", source = "encodedPassword")
    User registerRequestToUser(RegisterRequest request, String encodedPassword);

    @Mapping(target = "created_at", source = "createdAt")
    @Mapping(target = "updated_at", source = "updatedAt")
    UserDto userToUserDto(User user);

}
