package com.example.crud.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterBooking {
    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    Date start;

    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    Date end;

    String id_customer;
    String phone_booking;
    String id_room;
    int page;
    int size;
    String arrange;
}
