package com.example.crud.service.impl;

import com.example.crud.dto.request.CustomerRequest;
import com.example.crud.dto.response.CustomerResponse;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.ResponseFilter;
import com.example.crud.entity.CustomerEntity;
import com.example.crud.entity.RoomEntity;
import com.example.crud.mapping.CustomerMapping;
import com.example.crud.repository.BookingRepository;
import com.example.crud.repository.CustomerRepository;
import com.example.crud.service.serviceInterface.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    @Override
    public ResponseApi postCustomer(CustomerRequest customerRequest) {
        try {
            if (customerRequest.getName() == null || customerRequest.getName().trim().isEmpty()) {
                return new ResponseApi(false, "Tên không được để trống hoặc chỉ chứa khoảng trắng", null);
            }
            if (!customerRequest.getName().matches("^[^!@#$%^&*(),.?\":{}|<>]*$")) {
                return new ResponseApi(false, "Tên không được chứa ký tự đặc biệt", null);
            }
            if (customerRequest.getName().matches(".*\\d.*")) {
                return new ResponseApi(false, "Tên không được chứa số", null);
            }
            if (customerRequest.getName().length() < 3 || customerRequest.getName().length() > 20) {
                return new ResponseApi(false, "Tên phải có độ dài từ 3 đến 20 ký tự", null);
            }

            if (customerRequest.getPhone() == null || !customerRequest.getPhone().matches("^(01|02|03|08|09)\\d{8}$")) {
                return new ResponseApi(false, "Số điện thoại phải bắt đầu bằng 01, 02, 03, 08 hoặc 09 và có đúng 10 chữ số", null);
            }
            if (customerRequest.getAddress() == null || customerRequest.getAddress().trim().isEmpty()) {
                return new ResponseApi(false, "Địa chỉ không được để trống hoặc chỉ chứa khoảng trắng", null);
            }
            if (!customerRequest.getAddress().matches("^[^!@#$%^&*(),.?\":{}|<>]*$")) {
                return new ResponseApi(false, "Địa chỉ không được chứa ký tự đặc biệt", null);
            }
            if (customerRequest.getAddress().length() < 5 || customerRequest.getAddress().length() > 50) {
                return new ResponseApi(false, "Địa chỉ phải có độ dài từ 5 đến 50 ký tự", null);
            }
            if (customerRequest.getCccd() == null || !customerRequest.getCccd().matches("^\\d{12}$")) {
                return new ResponseApi(false, "CCCD phải là 12 chữ số và chỉ chứa số", null);
            }

            if (customerRepository.existsAllByCccd(customerRequest.getCccd())) {
                return new ResponseApi(false, "Cccd đã tồn tại", null);
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
            if (customerEntity == null) {
                return new ResponseApi(false, "Khách hàng không tồn tại", null);
            }

            // Validate customer request
            if (customerRequest.getName() == null || customerRequest.getName().trim().isEmpty()) {
                return new ResponseApi(false, "Tên không được để trống hoặc chỉ chứa khoảng trắng", null);
            }
            if (!customerRequest.getName().matches("^[^!@#$%^&*(),.?\":{}|<>]*$")) {
                return new ResponseApi(false, "Tên không được chứa ký tự đặc biệt", null);
            }
            if (customerRequest.getName().matches(".*\\d.*")) {
                return new ResponseApi(false, "Tên không được chứa số", null);
            }
            if (customerRequest.getName().length() < 3 || customerRequest.getName().length() > 20) {
                return new ResponseApi(false, "Tên phải có độ dài từ 3 đến 20 ký tự", null);
            }
            if (customerRequest.getPhone() == null || !customerRequest.getPhone().matches("^(01|02|03|08|09)\\d{8}$")) {
                return new ResponseApi(false, "Số điện thoại phải bắt đầu bằng 01, 02, 03, 08 hoặc 09 và có đúng 10 chữ số", null);
            }
            if (customerRequest.getAddress() == null || customerRequest.getAddress().trim().isEmpty()) {
                return new ResponseApi(false, "Địa chỉ không được để trống hoặc chỉ chứa khoảng trắng", null);
            }
            if (!customerRequest.getAddress().matches("^[^!@#$%^&*(),.?\":{}|<>]*$")) {
                return new ResponseApi(false, "Địa chỉ không được chứa ký tự đặc biệt", null);
            }
            if (customerRequest.getAddress().length() < 5 || customerRequest.getAddress().length() > 50) {
                return new ResponseApi(false, "Địa chỉ phải có độ dài từ 5 đến 50 ký tự", null);
            }
            if (customerRequest.getCccd() == null || !customerRequest.getCccd().matches("^\\d{12}$")) {
                return new ResponseApi(false, "CCCD phải là 12 chữ số và chỉ chứa số", null);
            }
            boolean cccdExists = customerRepository.existsByCccdAndIdNot(customerRequest.getCccd(), id);
            if (cccdExists) {
                return new ResponseApi(false, "Cccd đã tồn tại", null);
            }

            // Update the customer entity
            String oldPhone = customerEntity.getPhone();
            String oldCccd = customerEntity.getCccd();

            customerEntity.setName(customerRequest.getName());
            customerEntity.setPhone(customerRequest.getPhone());
            customerEntity.setAddress(customerRequest.getAddress());
            customerEntity.setCccd(customerRequest.getCccd());

            customerRepository.save(customerEntity);

            // Update bookings if necessary
            if (!oldCccd.equals(customerRequest.getCccd())) {
                bookingRepository.updateBookingsCccd(oldCccd, customerRequest.getCccd());
            }
            if (!oldPhone.equals(customerRequest.getPhone())) {
                bookingRepository.updateBookingsPhone(oldPhone, customerRequest.getPhone());
            }

            return new ResponseApi(true, "Chỉnh sửa dữ liệu thành công", customerEntity);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi deleteCustomer(Long id) {
        try {
            CustomerEntity customerEntity = customerRepository.findById(id).orElse(null);
            String customerValue = "";

            if (customerEntity == null) {
                return new ResponseApi(false, "Khách hàng không tồn tại không tồn tại", null);
            }

            customerValue = customerEntity.getCccd();

            if (bookingRepository.existsByIdCustomer(customerValue)) {
                return new ResponseApi(false, "Không thể xóa vì khách hàng được sử dụng ", null);
            }
            customerRepository.deleteById(id);
            return new ResponseApi(true, "xóa dữ liệu thành công", null);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseFilter filterCustomer(String name, String address, String phone, String cccd, int page, int size, String sortType) {
        try {
            Pageable pageable = PageRequest
                    .of(page, size, Sort.by(Sort.Direction.valueOf(sortType.toUpperCase()), "id"));
            Page<CustomerEntity> customerPage = customerRepository
                    .searchByNameOrAddressOrCccdOrPhone(name, phone, address, cccd, pageable);
            List<CustomerResponse> customerResponseList = customerPage.getContent()
                    .stream()
                    .map(CustomerMapping::mapEntityToResponse)
                    .collect(Collectors.toList());
            return new ResponseFilter(true, "Tìm kiếm dữ liệu", customerResponseList, customerPage.getTotalPages(), customerPage.getTotalElements());
        } catch (Exception e) {
            return new ResponseFilter(false, e.getMessage(), null, 0, 0);
        }
    }


}








