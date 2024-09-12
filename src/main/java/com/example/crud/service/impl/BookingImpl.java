package com.example.crud.service.impl;

import com.example.crud.dto.request.BookingRequest;
import com.example.crud.dto.request.FilterBooking;
import com.example.crud.dto.response.*;
import com.example.crud.entity.BookingEntity;
import com.example.crud.mapping.BookingMapping;
import com.example.crud.repository.BookingRepository;
import com.example.crud.repository.CustomerRepository;
import com.example.crud.repository.RoomRepository;
import com.example.crud.service.serviceInterface.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class BookingImpl implements BookingService {
    @Autowired
    public BookingRepository bookingRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public ResponseApi postBooking(BookingRequest bookingRequest) {
        try {
            if (bookingRequest.getId_customer() == null || !bookingRequest.getId_customer().matches("\\d{12}")) {
                return new ResponseApi(false, "Cần cung cấp đúng cccd của khách hàng (12 chữ số)", null);
            }

            if (bookingRequest.getPhone_booking() == null || !bookingRequest.getPhone_booking().matches("\\d{10}")) {
                return new ResponseApi(false, "Cần cung cấp đúng phone của khách hàng (10 số)", null);
            }

            if (bookingRequest.getId_room() == null || !bookingRequest.getId_room().matches("\\d{3,20}")) {
                return new ResponseApi(false, "Cần cung cấp đúng mã phòng (từ 3 đến 20 chữ số)", null);
            }

            boolean customerCccdExists = customerRepository.existsByCccd(bookingRequest.getId_customer());
            if (!customerCccdExists) {
                return new ResponseApi(false, "cần cung cấp đúng cccd của khách hàng", null);
            }

            boolean customerPhoneExists = customerRepository.existsByPhone(bookingRequest.getPhone_booking());
            if (!customerPhoneExists) {
                return new ResponseApi(false, "cần cung cấp đúng phone của khách hàng", null);
            }

            boolean roomExists = roomRepository.existsByRoom(bookingRequest.getId_room());
            if (!roomExists) {
                return new ResponseApi(false, "cần cung cấp đúng mã phòng", null);
            }

            if (bookingRequest.getStart() == null || bookingRequest.getEnd() == null) {
                return new ResponseApi(false, "Cần cung cấp cả ngày bắt đầu và ngày kết thúc", null);
            }

            if (bookingRequest.getEnd().before(bookingRequest.getStart())) {
                return new ResponseApi(false, "Ngày kết thúc phải lớn hơn ngày bắt đầu", null);
            }
            long conflictingCount = bookingRepository.countConflictingBookings(
                    bookingRequest.getId_room(),
                    bookingRequest.getStart(),
                    bookingRequest.getEnd()
            );

            if (conflictingCount > 0) {
                return new ResponseApi(false, "Khoảng thời gian bạn chọn nằm trong khoảng thời gian đã được đặt trước, vui lòng chọn khoảng thời gian khác", null);
            }

            long diffInMillis = bookingRequest.getEnd().getTime() -  bookingRequest.getStart().getTime();
            long numberOfDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            Long getRoomValue = bookingRepository.findRoomValueByRoomId(bookingRequest.getId_room());
            String getRoomStayString  = bookingRepository.findRoomStayByRoomId(bookingRequest.getId_room());
            Long getRoomStay = Long.valueOf(getRoomStayString);

            Long total = numberOfDays * (getRoomValue/getRoomStay);

            BookingEntity bookingEntity = BookingMapping.mapRequestToEntity(bookingRequest);
            bookingEntity.setTotal(total);
            bookingRepository.save(bookingEntity);
            return new ResponseApi(true, "done", bookingEntity);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi putBooking(Long id, BookingRequest bookingRequest) {
        try {
            BookingEntity bookingEntity = bookingRepository.findById(id).orElse(null);
            if (bookingRequest.getEnd().before(bookingRequest.getStart())) {
                return new ResponseApi(false, "Ngày kết thúc phải lớn hơn ngày bắt đầu", null);
            }
            boolean customerCccdExists = customerRepository.existsByCccd(bookingRequest.getId_customer());
            if (!customerCccdExists) {
                return new ResponseApi(false, "cần điền đúng cccd của customer", null);
            }
            bookingEntity.setId_customer(bookingRequest.getId_customer());

            boolean customerPhoneExists = customerRepository.existsByPhone(bookingRequest.getPhone_booking());
            if (!customerPhoneExists) {
                return new ResponseApi(false, "cần điền đúng phone của customer", null);
            }
            bookingEntity.setPhone_booking(bookingRequest.getPhone_booking());

            boolean roomExists = roomRepository.existsByRoom(bookingRequest.getId_room());
            if (!roomExists) {
                return new ResponseApi(false, "cần điền đúng mã phòng", null);
            }

            long conflictingCount = bookingRepository.countConflictingBookings(
                    bookingRequest.getId_room(),
                    bookingRequest.getStart(),
                    bookingRequest.getEnd()
            );

            if (conflictingCount > 0) {
                return new ResponseApi(false, "Khoảng thời gian bạn chọn nằm trong khoảng thời gian đã được đặt trước, vui lòng chọn khoảng thời gian khác", null);
            }

            long diffInMillis = bookingRequest.getEnd().getTime() -  bookingRequest.getStart().getTime();
            long numberOfDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            Long getRoomValue = bookingRepository.findRoomValueByRoomId(bookingRequest.getId_room());
            String getRoomStayString  = bookingRepository.findRoomStayByRoomId(bookingRequest.getId_room());
            Long getRoomStay = Long.valueOf(getRoomStayString);

            Long total = numberOfDays * (getRoomValue/getRoomStay);


            bookingEntity.setId_room(bookingRequest.getId_room());
            bookingEntity.setStart(bookingRequest.getStart());
            bookingEntity.setEnd(bookingRequest.getEnd());
            bookingEntity.setTotal(total);
            bookingRepository.save(bookingEntity);
            return new ResponseApi(true, "done", bookingEntity);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseApi deleteBooking(Long id) {
        try {
            bookingRepository.deleteById(id);
            return new ResponseApi(true, "Xoá lịch đặt phòng thành công", null);
        } catch (Exception e) {
            return new ResponseApi(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseFilter filterBooking(FilterBooking filterBooking) {
        try {
            Pageable pageable = PageRequest
                    .of(
                            filterBooking.getPage(), filterBooking.getSize(),
                            Sort.by(Sort.Direction.valueOf(filterBooking.getArrange().toUpperCase()), "id")
                    );
            Page<BookingEntity> bookingPage = bookingRepository.searchByStartOrEndOrId_customerOrId_room(
                    filterBooking.getStart(),
                    filterBooking.getEnd(),
                    filterBooking.getId_customer(),
                    filterBooking.getPhone_booking(),
                    filterBooking.getId_room(),
                    pageable
            );
            List<BookingResponse> bookingResponseList = bookingPage
                    .getContent()
                    .stream().map(BookingMapping::mapEntityToResponse)
                    .collect(Collectors.toList());
            return new ResponseFilter(true, "tìm kiếm thành công", bookingResponseList, bookingPage.getTotalPages(), bookingPage.getTotalElements());
        } catch (Exception e) {
            return new ResponseFilter(false, e.getMessage(), null, 0, 0);
        }
    }


}
