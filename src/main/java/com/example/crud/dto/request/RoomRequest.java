package com.example.crud.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRequest {
    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min = 3, max = 50, message = "name phai co do dai tu 3 -> 50 ky tu")
    String name;

    @NotBlank(message = "Mã phòng dùng không được để trống")
    @Size(min = 3, max = 50, message = "Mã phòng phai co do dai tu 3 -> 50 ky tu")
    String room;

    @Min(value = 200, message = "gia tri toi thieu la 100")
    @NotNull(message = "value không được để trống")
    Long value;

    @NotBlank(message = "status không được để trống")
    String status;

    @NotBlank(message = "Mã phòng dùng không được để trống")
    String stay;
}
