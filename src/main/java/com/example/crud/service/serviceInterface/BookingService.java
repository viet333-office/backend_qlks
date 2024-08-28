package com.example.crud.service.serviceInterface;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.ResponseFilter;

import java.sql.Date;

public interface BookingService {
    ResponseApi getBooking();

    ResponseApi postBooking(BookingRequest bookingRequest);

    ResponseApi putBooking(Long id, BookingRequest bookingRequest);

    ResponseApi deleteBooking(Long id);

    ResponseFilter filterBooking(Date start , Date end, String id_customer, String id_room , String arrange, int page , int size);
}
