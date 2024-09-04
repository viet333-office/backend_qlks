package com.example.crud.controller;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.request.FilterBooking;
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

    @PostMapping("/filter")
    public ResponseEntity<?> filterBooking(@RequestBody FilterBooking filterBooking) {
        return new ResponseEntity<>(bookingService
                .filterBooking(filterBooking), HttpStatus.OK);
    }

}
