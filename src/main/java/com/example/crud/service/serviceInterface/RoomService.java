package com.example.crud.service.serviceInterface;

import com.example.crud.dto.request.RoomRequest;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.ResponseFilter;


public interface RoomService {
    ResponseApi getRoom();

    ResponseApi postRoom(RoomRequest roomRequest);

    ResponseApi putRoom(Long id, RoomRequest roomRequest);

    ResponseApi deleteRoom(Long id);


    ResponseFilter filterRoom(String name, String room, Long value , String status, String stay, int page, int size, String arrange);
}
