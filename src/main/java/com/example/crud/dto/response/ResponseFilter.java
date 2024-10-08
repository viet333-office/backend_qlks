package com.example.crud.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseFilter {
    Boolean status;
    String message;
    Object content;
     int totalPages;
     long totalItems;
}
