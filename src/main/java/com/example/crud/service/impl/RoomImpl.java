package com.example.crud.service.impl;

import com.example.crud.dto.request.RoomRequest;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.ResponseFilter;
import com.example.crud.dto.response.RoomResponse;
import com.example.crud.entity.BookingEntity;
import com.example.crud.entity.RoomEntity;
import com.example.crud.mapping.RoomMapping;
import com.example.crud.repository.BookingRepository;
import com.example.crud.repository.RoomRepository;
import com.example.crud.service.serviceInterface.RoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class RoomImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
    @Override
    public ResponseApi getRoom() {
        try {
            return new ResponseApi(true, "done", roomRepository.findAll());
        } catch (Exception e) {
            return new ResponseApi(false, "bug ne", null);
        }

    }

    @Override
    public ResponseApi postRoom(RoomRequest roomRequest) {
        try {
            if (isNumeric(roomRequest.getName())) {
                return new ResponseApi(false, "name room chỉ chữ", null);
            }
            if (!isNumeric(roomRequest.getRoom())) {
                return new ResponseApi(false, "số room chỉ chứa số", null);
            }

            RoomEntity roomEntity = RoomMapping.mapRequestToEntity(roomRequest);
            roomRepository.save(roomEntity);
            return new ResponseApi(true, "done", roomEntity);
        } catch (Exception e) {
            return new ResponseApi(false, "bug ne", null);
        }
    }

    @Override
    public ResponseApi putRoom(Long id, RoomRequest roomRequest) {
        try {

            RoomEntity roomEntity = roomRepository.findById(id).get();
            roomEntity.setName(roomRequest.getName());
            roomEntity.setRoom(roomRequest.getRoom());
            roomEntity.setValue(roomRequest.getValue());
            roomEntity.setStatus(roomRequest.getStatus());
            roomEntity.setStay(roomRequest.getStay());

            roomRepository.save(roomEntity);
            return new ResponseApi(true, "done", null);
        } catch (Exception e) {
            return new ResponseApi(false, "bug ne", null);
        }
    }

    @Override
    public ResponseApi deleteRoom(Long id) {
        try {
            List<BookingEntity> bookings = bookingRepository.findByRoomId(id);
            if (!bookings.isEmpty()) {
                bookingRepository.deleteByRoomId(id);
            }
            roomRepository.deleteById(id);
            return new ResponseApi(true, "done", null);
        } catch (Exception e) {
            return new ResponseApi(false, "bug ne", null);
        }
    }

    @Override
    public ResponseFilter filterRoom(String name, String room, Long value, String status, String stay, int page, int size, String arrange) {
        try {
            Pageable pageable = PageRequest
                    .of(
                            page, size,
                            Sort.by(Sort.Direction.valueOf(arrange.toUpperCase()), "room")
                    );
            Page<RoomEntity> roomPage = roomRepository.filterRoom(name, room, value, status, stay, pageable);
            List<RoomResponse> roomResponseList = roomPage.getContent()
                    .stream()
                    .map(RoomMapping::mapEntityToResponse)
                    .collect(Collectors.toList());

            return new ResponseFilter(true, "done", roomResponseList, roomPage.getTotalPages(), roomPage.getTotalElements());
        } catch (Exception e) {
            return new ResponseFilter(false, "bug ne", null, 0, 0);
        }
    }


}
