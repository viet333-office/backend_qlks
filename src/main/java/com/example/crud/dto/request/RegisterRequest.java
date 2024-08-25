package com.example.crud.dto.request;

import com.example.crud.constant.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    Role role;
}
