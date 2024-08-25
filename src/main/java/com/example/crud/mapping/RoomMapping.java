package com.example.crud.mapping;

import com.example.crud.dto.request.RoomRequest;
import com.example.crud.dto.response.RoomResponse;
import com.example.crud.entity.RoomEntity;

public class RoomMapping {
    public static RoomEntity mapRequestToEntity(RoomRequest roomRequest) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoom(roomRequest.getRoom());
        roomEntity.setName(roomRequest.getName());
        roomEntity.setValue(roomRequest.getValue());
        roomEntity.setStatus(roomRequest.getStatus());
        roomEntity.setStay(roomRequest.getStay());
        return roomEntity;
    }

    public static RoomResponse mapEntityToResponse(RoomEntity roomEntity) {
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(roomEntity.getId());
        roomResponse.setRoom(roomEntity.getRoom());
        roomResponse.setName(roomEntity.getName());
        roomResponse.setValue(roomEntity.getValue());
        roomResponse.setStatus(roomEntity.getStatus());
        roomResponse.setStay(roomEntity.getStay());
        return roomResponse;
    }
}
