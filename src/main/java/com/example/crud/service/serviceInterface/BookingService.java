package com.example.crud.service.serviceInterface;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.request.FilterBooking;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.ResponseFilter;

import java.sql.Date;

public interface BookingService {

    ResponseApi postBooking(BookingRequest bookingRequest);

    ResponseApi putBooking(Long id, BookingRequest bookingRequest);

    ResponseApi deleteBooking(Long id);

    ResponseFilter filterBooking(FilterBooking filterBooking);
}
