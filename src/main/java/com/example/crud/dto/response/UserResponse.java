package com.example.crud.dto.response;

import com.example.crud.constant.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String firstname;
    String lastname;
    String email;
    String password;
    Role role;
}
