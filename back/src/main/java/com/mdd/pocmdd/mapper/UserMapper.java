package com.mdd.pocmdd.mapper;

import com.mdd.pocmdd.models.User;
import org.mapstruct.Mapper;
import com.mdd.pocmdd.dto.UserDTO;


@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {

}
