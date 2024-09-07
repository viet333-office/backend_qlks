package com.example.crud.service.impl;

import com.example.crud.dto.request.RoomRequest;
import com.example.crud.dto.response.ResponseApi;
import com.example.crud.dto.response.ResponseFilter;
import com.example.crud.dto.response.RoomResponse;
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



    @Override
    public ResponseApi postRoom(RoomRequest roomRequest) {
        try {
            if (roomRequest.getName().length() < 3 || roomRequest.getName().length() > 20) {
                return new ResponseApi(false, "Tên phải có độ dài từ 3 đến 20 ký tự", null);
            }
            if (roomRequest.getName().matches(".*\\d.*")) {
                return new ResponseApi(false, "Tên phòng không được chứa số", null);
            }

            if (!roomRequest.getRoom().matches("^\\d{3,20}$")) {
                return new ResponseApi(false, "Số phòng chỉ chứa số, độ dài từ 3 đến 20 ký tự", null);
            }

            if (roomRequest.getValue() < 1 || roomRequest.getValue() > 9999999999L) {
                return new ResponseApi(false, "Giá trị phòng phải nằm trong khoảng từ 1 đến 9999999999", null);
            }

            if (roomRequest.getStay().length() < 1 || roomRequest.getStay().length() > 100) {
                return new ResponseApi(false, "Thời gian lưu trú phải có độ dài từ 1 đến 100 ký tự", null);
            }
            if (!roomRequest.getStay().matches("^\\d+$")) {
                return new ResponseApi(false, "Thời gian lưu trú phải là số", null);
            }
            if (roomRepository.existsAllByRoom(roomRequest.getRoom())) {
                return new ResponseApi(false, "Dữ liệu đã tồn tại", null);
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
            if (roomRequest.getName() == null || roomRequest.getName().trim().isEmpty()) {
                return new ResponseApi(false, "Tên không được để trống hoặc chỉ chứa khoảng trắng", null);
            }
            
            if (roomRequest.getName().length() < 3 || roomRequest.getName().length() > 20) {
                return new ResponseApi(false, "Tên phải có độ dài từ 3 đến 20 ký tự", null);
            }

            if (!roomRequest.getName().matches("^[^!@#$%^&*(),.?\":{}|<>]*$")) {
                return new ResponseApi(false, "Tên không được chứa ký tự đặc biệt", null);
            }

            if (!roomRequest.getRoom().matches("^\\d{3,20}$")) {
                return new ResponseApi(false, "Số phòng chỉ chứa số, độ dài từ 3 đến 20 ký tự", null);
            }

            if (roomRequest.getValue() < 1 || roomRequest.getValue() > 9999999999L) {
                return new ResponseApi(false, "Giá trị phòng phải nằm trong khoảng từ 1 đến 9999999999", null);
            }

            if (roomRequest.getStay().length() < 1 || roomRequest.getStay().length() > 100) {
                return new ResponseApi(false, "Thời gian lưu trú phải có độ dài từ 1 đến 100 ký tự", null);
            }
            if (!roomRequest.getStay().matches("^\\d+$")) {
                return new ResponseApi(false, "Thời gian lưu trú phải là số", null);
            }
            boolean roomExists = roomRepository.existsByRoomAndIdNot(roomRequest.getRoom(), id);
            if (roomExists) {
                return new ResponseApi(false, "Dữ liệu đã tồn tại", null);
            }
            String oldRoom = roomEntity.getRoom();

            roomEntity.setName(roomRequest.getName());
            roomEntity.setRoom(roomRequest.getRoom());
            roomEntity.setValue(roomRequest.getValue());
            roomEntity.setStatus(roomRequest.getStatus());
            roomEntity.setStay(roomRequest.getStay());

            roomRepository.save(roomEntity);
            if (!oldRoom.equals(roomRequest.getRoom())) {
                bookingRepository.updateBookingsRoom(oldRoom, roomRequest.getRoom());
            }
            return new ResponseApi(true, "done", roomEntity);
        } catch (Exception e) {
            return new ResponseApi(false, "bug ne", null);
        }
    }

    @Override
    public ResponseApi deleteRoom(Long id) {
        try {
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
                    .of(page, size, Sort.by(Sort.Direction.valueOf(arrange.toUpperCase()), "room"));
            Page<RoomEntity> roomPage = roomRepository.searchByNameOrRoomOrValueOrStatusOrStay(name, room, value, status, stay, pageable);
            List<RoomResponse> roomResponseList = roomPage.getContent()
                    .stream()
                    .map(RoomMapping::mapEntityToResponse).collect(Collectors.toList());
            return new ResponseFilter(true, "done", roomResponseList, roomPage.getTotalPages(), roomPage.getTotalElements());
        } catch (Exception e) {
            return new ResponseFilter(false, "bug ne", null, 0, 0);
        }
    }


}
