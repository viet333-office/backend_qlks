package com.example.crud.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequest {
    @NotBlank(message = "Tên người dùng không được để trống")
    String name;

    @NotBlank(message = "cccd dân không được để trống")
    String cccd;

    @NotBlank(message = "address dân không được để trống")
    String address;

    @NotBlank(message = "số điện thoại liên hệ không được để trống")
    String phone;
}
