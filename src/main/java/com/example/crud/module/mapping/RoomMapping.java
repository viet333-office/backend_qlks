package com.example.crud.module.mapping;

import com.example.crud.module.dto.request.RoomRequest;
import com.example.crud.module.dto.response.RoomResponse;
import com.example.crud.module.entity.RoomEntity;

public class RoomMapping {
    public static RoomEntity mapRequestToEntity(RoomRequest roomRequest) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setName(roomRequest.getName());
        roomEntity.setRoom(roomRequest.getRoom());
        roomEntity.setValue(roomRequest.getValue());
        roomEntity.setStatus(roomRequest.getStatus());
        roomEntity.setStay(roomRequest.getStay());
        return roomEntity;
    }

    public static RoomResponse mapEntityToResponse(RoomEntity roomEntity) {
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(roomEntity.getId());
        roomResponse.setName(roomEntity.getName());
        roomResponse.setRoom(roomEntity.getRoom());
        roomResponse.setValue(roomEntity.getValue());
        roomResponse.setStatus(roomEntity.getStatus());
        roomResponse.setStay(roomEntity.getStay());
        return roomResponse;
    }
}
