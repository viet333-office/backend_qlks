package com.example.crud.module.service.serviceInterface;

import com.example.crud.module.dto.request.CustomerRequest;
import com.example.crud.module.dto.response.ResponseApi;
import com.example.crud.module.dto.response.ResponseFilter;

public interface CustomerService {

    ResponseApi postCustomer(CustomerRequest customerRequest);

    ResponseApi putCustomer(Long id, CustomerRequest customerRequest);

    ResponseApi deleteCustomer(Long id);

    ResponseFilter filterCustomer(String name, String address, String phone, String cccd, int page, int size, String sortType);

}
