package com.example.crud.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    Date start;

    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    Date end;

    String id_customer;
    String phone_booking;
    String id_room;
}
