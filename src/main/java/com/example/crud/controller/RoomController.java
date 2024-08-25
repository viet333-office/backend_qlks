package com.example.crud.controller;

import com.example.crud.dto.request.RoomRequest;
import com.example.crud.service.serviceInterface.RoomService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/getRoom")
    public ResponseEntity<?> getRoom() {
        return new ResponseEntity<>(roomService.getRoom(), HttpStatus.OK);
    }

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
            @Nullable
            @RequestParam String name,
            @Nullable
            @RequestParam String room,
            @Nullable
            @RequestParam Long value,
            @Nullable
            @RequestParam String status,
            @Nullable
            @RequestParam String stay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @Nullable
            @RequestParam String arrange

    ) {
        return new ResponseEntity<>(roomService.filterRoom(name,room, value, status,stay,page, size, arrange),HttpStatus.OK);
    }

}
