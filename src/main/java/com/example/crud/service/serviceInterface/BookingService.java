package com.example.crud.service.serviceInterface;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.entity.CustomerEntity;
import com.example.crud.entity.RoomEntity;

import java.sql.Date;

public interface BookingService {
    ResponseApi getBooking();

    ResponseApi postBooking(BookingRequest bookingRequest);

    ResponseApi putBooking(Long id, BookingRequest bookingRequest);

    ResponseApi deleteBooking(Long id);

    ResponseApi filterBooking(Date start , Date end, Long id_customer, Long id_room , String arrange, int page , int size);
}
