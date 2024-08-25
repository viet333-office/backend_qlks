package com.example.crud.controller;

import com.example.crud.dto.request.CustomerRequest;
import com.example.crud.service.serviceInterface.CustomerService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    public CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<?> getCustomer() {
        return new ResponseEntity<>(customerService.getCustomer(), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        return new ResponseEntity<>(customerService.postCustomer(customerRequest), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(@RequestParam Long id, @Valid @RequestBody CustomerRequest customerRequest) {
        System.out.println("đã vào controller");
        return new ResponseEntity<>(customerService.putCustomer(id, customerRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return new ResponseEntity<>(customerService.deleteCustomer(id), HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterCustomer(

            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String phone,
            @RequestParam(defaultValue = "") String address,
            @RequestParam(defaultValue = "") String cccd,
            @Nullable
            @RequestParam(defaultValue = "asc") String sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return new ResponseEntity<>(customerService.filterCustomer(name, address, phone, cccd, sortType, page, size), HttpStatus.OK);
    }


}
