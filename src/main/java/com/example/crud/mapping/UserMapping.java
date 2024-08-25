package com.example.crud.mapping;

import com.example.crud.dto.request.RegisterRequest;
import com.example.crud.dto.response.UserResponse;
import com.example.crud.entity.UserEntity;

public class UserMapping {
    public static UserEntity mapRequestToEntity(RegisterRequest registerRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(registerRequest.getFirstname());
        userEntity.setLastname(registerRequest.getLastname());
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(registerRequest.getPassword());
        userEntity.setRole(registerRequest.getRole());
        return userEntity;
    }

    public static UserResponse mapEntityToResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setFirstname(userEntity.getFirstname());
        userResponse.setLastname(userEntity.getLastname());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setPassword(userEntity.getPassword());
        userResponse.setRole(userEntity.getRole());
        return userResponse;
    }
}
