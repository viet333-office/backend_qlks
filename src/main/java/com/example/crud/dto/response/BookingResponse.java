package com.example.crud.dto.response;

import com.example.crud.entity.CustomerEntity;
import com.example.crud.entity.RoomEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingResponse {
    Long id;
    Date start;
    Date end;
    Long id_customer;
    Long id_room;
}
