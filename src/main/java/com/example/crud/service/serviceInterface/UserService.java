package com.example.crud.service.serviceInterface;

import com.example.crud.dto.request.RegisterRequest;
import com.example.crud.dto.response.ResponseApi;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseApi putUser(Long id , RegisterRequest registerRequest);
    ResponseApi deleteUser(Long id );
}
