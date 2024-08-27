package com.example.crud.dto.response;

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
