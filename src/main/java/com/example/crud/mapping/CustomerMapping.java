package com.example.crud.mapping;

import com.example.crud.dto.request.CustomerRequest;
import com.example.crud.dto.response.CustomerResponse;
import com.example.crud.entity.CustomerEntity;

public class CustomerMapping {
    public static CustomerEntity mapRequestToEntity(CustomerRequest customerRequest) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(customerRequest.getName());
        customerEntity.setAddress(customerRequest.getAddress());
        customerEntity.setPhone(customerRequest.getPhone());
        customerEntity.setCccd(customerRequest.getCccd());
        return customerEntity;
    }

    public static CustomerResponse mapEntityToResponse(CustomerEntity customerEntity) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customerEntity.getId());
        customerResponse.setName(customerEntity.getName());
        customerResponse.setAddress(customerEntity.getAddress());
        customerResponse.setPhone(customerEntity.getPhone());
        customerResponse.setCccd(customerEntity.getCccd());
        return customerResponse;
    }

}
