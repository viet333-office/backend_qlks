package com.example.crud.service.serviceInterface;

import com.example.crud.dto.request.CustomerRequest;
import com.example.crud.dto.response.ResponseApi;

public interface CustomerService {
    ResponseApi getCustomer();

    ResponseApi postCustomer(CustomerRequest customerRequest);

    ResponseApi putCustomer(Long id, CustomerRequest customerRequest);

    ResponseApi deleteCustomer(Long id);

    ResponseApi filterCustomer(String name, String address, String phone,String cccd, String sortType, int page, int size);

}
