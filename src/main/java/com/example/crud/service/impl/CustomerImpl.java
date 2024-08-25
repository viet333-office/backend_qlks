package com.example.crud.service.impl;

import com.example.crud.dto.request.CustomerRequest;
import com.example.crud.dto.response.CustomerResponse;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.entity.BookingEntity;
import com.example.crud.entity.CustomerEntity;
import com.example.crud.mapping.CustomerMapping;
import com.example.crud.repository.BookingRepository;
import com.example.crud.repository.CustomerRepository;
import com.example.crud.service.serviceInterface.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class CustomerImpl implements CustomerService {
    @Autowired
    public CustomerRepository customerRepository;
    @Autowired
    public BookingRepository bookingRepository;
    private boolean isNumeric(String str) {
        return  str.matches("\\d+");
    }
    @Override
    public ResponseApi getCustomer() {
        try {
            return new ResponseApi(true, "Lấy dữ liệu thành công", customerRepository.findAll());
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }

    }

    @Override
    public ResponseApi postCustomer(CustomerRequest customerRequest) {
        try {
            if (!isNumeric(customerRequest.getCccd())) {
                return new ResponseApi(false, "CCCD chỉ chứa số", null);
            }
            if (!isNumeric(customerRequest.getPhone())) {
                return new ResponseApi(false, "Số điện thoại chỉ chứa số", null);
            }
            if (customerRepository.existsAllByCccd(customerRequest.getCccd())) {
                return new ResponseApi(false, "Dữ liệu đã tồn tại", null);
            }

            CustomerEntity customerEntity = CustomerMapping.mapRequestToEntity(customerRequest);
            customerRepository.save(customerEntity);
            return new ResponseApi(true, "thêm dữ liệu thành công", customerEntity);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi putCustomer(Long id, CustomerRequest customerRequest) {
        try {
            CustomerEntity customerEntity = customerRepository.findById(id).orElse(null);
            if (!isNumeric(customerRequest.getCccd())) {
                return new ResponseApi(false, "CCCD phải chỉ chứa số", null);
            }
            if (!isNumeric(customerRequest.getPhone())) {
                return new ResponseApi(false, "Số điện thoại phải chỉ chứa số", null);
            }
            if (customerRepository.existsAllByCccdAndIdNot(customerRequest.getPhone(), id)) {
                return new ResponseApi(false, "Dữ liệu đã tồn tại", null);
            }
            List<BookingEntity> bookings = bookingRepository.findByCustomerId(id);
            customerEntity.setName(customerRequest.getName());
            customerEntity.setAddress(customerRequest.getAddress());
            customerEntity.setCccd(customerRequest.getCccd());
            customerEntity.setPhone(customerRequest.getPhone());
            customerRepository.save(customerEntity);

            for (BookingEntity booking : bookings) {
                booking.setId_customer(customerEntity.getId());
            }
            bookingRepository.saveAll(bookings);
            return new ResponseApi(true, "chỉnh sửa dữ liệu thành công", customerEntity);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi deleteCustomer(Long id) {
        try {
            List<BookingEntity> bookings = bookingRepository.findByCustomerId(id);
            if (!bookings.isEmpty()) {
                bookingRepository.deleteByCustomerId(id);
            }
            customerRepository.deleteById(id);
            return new ResponseApi(true, "xóa dữ liệu thành công", null);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi filterCustomer(String name ,String address, String phone,String cccd, String sortType, int page, int size) {
        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortType.toUpperCase()), "name"));
            List<CustomerEntity> customerEntityList = customerRepository.searchByNameOrAddressOrCccdOrPhone(name, phone, address,cccd, pageable);
            List<CustomerResponse> customerResponseList = customerEntityList
                    .stream()
                    .map(CustomerMapping::mapEntityToResponse)
                    .collect(Collectors.toList());

            return new ResponseApi(true, "tìm kiếm dữ liệu", customerResponseList);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }


}








