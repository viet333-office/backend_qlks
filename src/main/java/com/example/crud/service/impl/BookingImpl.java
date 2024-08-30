package com.example.crud.service.impl;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.response.*;
import com.example.crud.entity.BookingEntity;
import com.example.crud.mapping.BookingMapping;
import com.example.crud.repository.BookingRepository;
import com.example.crud.repository.CustomerRepository;
import com.example.crud.repository.RoomRepository;
import com.example.crud.service.serviceInterface.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseApi postBooking(BookingRequest bookingRequest) {
        try {
            boolean customerExists = customerRepository.existsByCccd(bookingRequest.getId_customer());
            if (!customerExists) {
                return new ResponseApi(false, "no id in customer", null);
            }

            boolean roomExists = roomRepository.existsByRoom(bookingRequest.getId_room());
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

            boolean customerExists = customerRepository.existsByCccd(bookingRequest.getId_customer());
            if (!customerExists) {
                return new ResponseApi(false, "no id in customer", null);
            }
            bookingEntity.setId_customer(bookingRequest.getId_customer());
            boolean roomExists = roomRepository.existsByRoom(bookingRequest.getId_room());
            if (!roomExists) {
                return new ResponseApi(false, "no id in room", null);
            }
            bookingEntity.setId_room(bookingRequest.getId_room());
            bookingEntity.setStart(bookingRequest.getStart());
            bookingEntity.setEnd(bookingRequest.getEnd());
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
    public ResponseFilter filterBooking(Date start, Date end, String id_customer, String id_room, String arrange, int page, int size) {
        try {
            Pageable pageable = PageRequest
                    .of(
                            page, size,
                            Sort.by(Sort.Direction.valueOf(arrange.toUpperCase()), "start")
                    );

            Page<BookingEntity> bookingPage = bookingRepository.searchByStartOrEndOrId_customerOrId_room(start, end, id_customer, id_room, pageable);
            List<BookingResponse> bookingResponseList = bookingPage
                    .getContent()
                    .stream().map(BookingMapping::mapEntityToResponse)
                    .collect(Collectors.toList());
            return new ResponseFilter(true, "done", bookingResponseList,bookingPage.getTotalPages(),bookingPage.getTotalElements());
        } catch (Exception e) {
            return new ResponseFilter(false, "bug ne", null,0,0);
        }
    }


}
