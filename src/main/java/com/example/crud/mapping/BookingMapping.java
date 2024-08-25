package com.example.crud.mapping;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.request.CustomerRequest;
import com.example.crud.dto.response.BookingResponse;
import com.example.crud.dto.response.CustomerResponse;
import com.example.crud.entity.BookingEntity;
import com.example.crud.entity.CustomerEntity;
import com.example.crud.entity.RoomEntity;

public class BookingMapping {
    public static BookingEntity mapRequestToEntity(BookingRequest bookingRequest) {
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setStart(bookingRequest.getStart());
        bookingEntity.setEnd(bookingRequest.getEnd());
        bookingEntity.setId_customer(bookingRequest.getId_customer());
        bookingEntity.setId_room(bookingRequest.getId_room());

        return bookingEntity;
    }

    public static BookingResponse mapEntityToResponse(BookingEntity bookingEntity) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setId(bookingEntity.getId());
        bookingResponse.setStart(bookingEntity.getStart());
        bookingResponse.setEnd(bookingEntity.getEnd());
        bookingResponse.setId_customer(bookingEntity.getId_customer());
        bookingResponse.setId_room(bookingEntity.getId_room());

        return bookingResponse;
    }
}
