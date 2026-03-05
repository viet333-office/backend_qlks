package com.example.crud.module.service.serviceInterface;

import com.example.crud.module.dto.request.BookingRequest;
import com.example.crud.module.dto.request.FilterBooking;
import com.example.crud.module.dto.response.ResponseApi;
import com.example.crud.module.dto.response.ResponseFilter;


public interface BookingService {

    ResponseApi postBooking(BookingRequest bookingRequest);

    ResponseApi putBooking(Long id, BookingRequest bookingRequest);

    ResponseApi deleteBooking(Long id);

    ResponseFilter filterBooking(FilterBooking filterBooking);
}
