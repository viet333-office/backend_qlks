package com.example.crud.module.service.serviceInterface;

import com.example.crud.module.dto.request.RegisterRequest;
import com.example.crud.module.dto.response.ResponseApi;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseApi putUser(Long id , RegisterRequest registerRequest);
    ResponseApi deleteUser(Long id );
}
