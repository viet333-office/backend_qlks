package com.example.crud.service.impl;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.response.BookingResponse;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.RoomResponse;
import com.example.crud.entity.BookingEntity;
import com.example.crud.entity.CustomerEntity;
import com.example.crud.entity.RoomEntity;
import com.example.crud.mapping.BookingMapping;
import com.example.crud.mapping.RoomMapping;
import com.example.crud.repository.BookingRepository;
import com.example.crud.repository.CustomerRepository;
import com.example.crud.repository.RoomRepository;
import com.example.crud.service.serviceInterface.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingImpl implements BookingService {
    @Autowired
    public BookingRepository bookingRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public ResponseApi getBooking() {
        try {
            return new ResponseApi(true, "done", bookingRepository.findAll());
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi postBooking(BookingRequest bookingRequest) {
        try {
            boolean customerExists = customerRepository.existsById(bookingRequest.getId_customer());
            if (!customerExists) {
                return new ResponseApi(false, "no id in customer", null);
            }

            boolean roomExists = roomRepository.existsById(bookingRequest.getId_room());
            if (!roomExists) {
                return new ResponseApi(false, "no id in room", null);
            }

            BookingEntity bookingEntity = BookingMapping.mapRequestToEntity(bookingRequest);
            bookingRepository.save(bookingEntity);
            return new ResponseApi(true, "done", bookingEntity);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi putBooking(Long id, BookingRequest bookingRequest) {
        try {
            BookingEntity bookingEntity = bookingRepository.findById(id).get();
            bookingEntity.setStart(bookingRequest.getStart());
            bookingEntity.setEnd(bookingRequest.getEnd());
            boolean customerExists = customerRepository.existsById(bookingRequest.getId_customer());
            if (!customerExists) {
                return new ResponseApi(false, "no id in customer", null);
            }
            bookingEntity.setId_customer(bookingRequest.getId_customer());
            boolean roomExists = roomRepository.existsById(bookingRequest.getId_room());
            if (!roomExists) {
                return new ResponseApi(false, "no id in room", null);
            }
            bookingEntity.setId_room(bookingRequest.getId_room());
            bookingRepository.save(bookingEntity);
            return new ResponseApi(true, "done", null);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi deleteBooking(Long id) {
        try {
            bookingRepository.deleteById(id);
            return new ResponseApi(true, "done", null);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi filterBooking(Date start, Date end, Long id_customer, Long id_room, String arrange, int page, int size) {
        try {
            Pageable pageable = PageRequest
                    .of(
                            page, size,
                            Sort.by(Sort.Direction.valueOf(arrange.toUpperCase()), "start")
                    );
            List<BookingEntity> bookingEntityList = bookingRepository.filter(start, end, id_customer, id_room, pageable);
            List<BookingResponse> bookingResponseList = bookingEntityList.stream().map(BookingMapping::mapEntityToResponse).collect(Collectors.toList());
            return new ResponseApi(true, "done", bookingResponseList);
        } catch (Exception e) {
            return new ResponseApi(false, "bug ne", null);
        }
    }


}
