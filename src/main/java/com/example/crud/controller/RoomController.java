package com.example.crud.controller;

import com.example.crud.dto.request.RoomRequest;
import com.example.crud.service.serviceInterface.RoomService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    public RoomService roomService;

    @PostMapping("/postRoom")
    public ResponseEntity<?> postRoom(@Valid @RequestBody RoomRequest roomRequest) {
        return new ResponseEntity<>(roomService.postRoom(roomRequest), HttpStatus.OK);
    }

    @PutMapping("/putRoom")
    public ResponseEntity<?> putRoom(@RequestParam Long id, @RequestBody RoomRequest roomRequest) {
        return new ResponseEntity<>(roomService.putRoom(id, roomRequest), HttpStatus.OK);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return new ResponseEntity<>(roomService.deleteRoom(id), HttpStatus.OK);
    }


    @GetMapping("/filter")
    public ResponseEntity<?> filterRoom(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String room,
            @RequestParam(defaultValue = "0") Long value,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String stay,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "asc") String arrange

    ) {
        System.out.println(name);
        return new ResponseEntity<>(roomService.filterRoom(name,room, value, status,stay,page, size, arrange),HttpStatus.OK);
    }

}
