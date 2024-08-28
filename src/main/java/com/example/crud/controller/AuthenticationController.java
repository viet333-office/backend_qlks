package com.example.crud.controller;

import com.example.crud.dto.request.AuthenticationRequest;
import com.example.crud.dto.request.RegisterRequest;
import com.example.crud.dto.response.AuthenticationResponse;
import com.example.crud.service.impl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
     final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @org.springframework.web.bind.annotation.RequestBody RegisterRequest registerRequest
            ){
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticationResponseResponseEntity(
            @RequestBody AuthenticationRequest authenticationRequest
            ){
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }

    @PostMapping("/refresh-" +
            "")
    public void refreshToken(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        service.refreshToken(httpServletRequest,httpServletResponse);
    }


}
