package com.example.crud.service.impl;

import com.example.crud.dto.request.RegisterRequest;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.entity.UserEntity;
import com.example.crud.repository.UserRepository;
import com.example.crud.service.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserImpl implements UserService {
    final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public UserImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseApi putUser(Long id, RegisterRequest registerRequest) {
        try {
            UserEntity userEntity = userRepository.findById(id).get();
            userEntity.setFirstname(registerRequest.getFirstname());
            userEntity.setLastname(registerRequest.getLastname());
            userEntity.setEmail(registerRequest.getEmail());
            userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            userRepository.save(userEntity);
            return new ResponseApi(true,"done",userEntity);
        }catch (Exception e){
            return new ResponseApi(false,e.getMessage(),null);
        }

    }

    @Override
    public ResponseApi deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseApi(false,"done",null);
        }catch (Exception e){
            return new ResponseApi(false,e.getMessage(),null);
        }
    }
}
