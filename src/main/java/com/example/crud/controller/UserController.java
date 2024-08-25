package com.example.crud.controller;


import com.example.crud.dto.request.RegisterRequest;
import com.example.crud.service.serviceInterface.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private UserService userService;
    @PutMapping("/put")
    public ResponseEntity<?> putUser(@RequestParam Long id, @Valid @RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(userService.putUser(id, registerRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

}
