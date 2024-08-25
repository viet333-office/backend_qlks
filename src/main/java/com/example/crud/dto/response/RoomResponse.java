package com.example.crud.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomResponse {
    Long id;
    String room;
    String name;
    Long value;
    String status;
    String stay;
}
