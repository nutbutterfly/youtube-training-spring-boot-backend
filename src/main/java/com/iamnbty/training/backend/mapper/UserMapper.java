package com.iamnbty.training.backend.mapper;

import com.iamnbty.training.backend.entity.User;
import com.iamnbty.training.backend.model.MRegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    MRegisterResponse toRegisterResponse(User user);

}
