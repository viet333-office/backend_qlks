package com.example.crud.service.serviceInterface;

import com.example.crud.dto.request.CustomerRequest;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.ResponseFilter;

public interface CustomerService {
    ResponseApi getCustomer();

    ResponseApi postCustomer(CustomerRequest customerRequest);

    ResponseApi putCustomer(Long id, CustomerRequest customerRequest);

    ResponseApi deleteCustomer(Long id);

    ResponseFilter filterCustomer(String name, String address, String phone, String cccd, int page, int size, String sortType);

}
