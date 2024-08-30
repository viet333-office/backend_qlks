package com.example.crud.controller;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.service.impl.BookingImpl;
import com.example.crud.service.serviceInterface.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Controller
@CrossOrigin
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    public BookingService bookingService;


    @PostMapping("/postBooking")
    public ResponseEntity<?> postBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return new ResponseEntity<>(bookingService.postBooking(bookingRequest), HttpStatus.OK);
    }

    @PutMapping("/putBooking")
    public ResponseEntity<?> putBooking(@RequestParam Long id, @Valid @RequestBody BookingRequest bookingRequest) {
        return new ResponseEntity<>(bookingService.putBooking(id, bookingRequest), HttpStatus.OK);
    }

    @DeleteMapping("/deleteBooking")
    public ResponseEntity<?> deleteBooking(@RequestParam Long id) {
        return new ResponseEntity<>(bookingService.deleteBooking(id), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterBooking(

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end,
            @RequestParam String id_customer,
            @RequestParam String id_room,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam String arrange
    ) {
        return new ResponseEntity<>(bookingService
                .filterBooking(start, end, id_customer, id_room, arrange, page, size), HttpStatus.OK);
    }

}
