package com.example.crud.entity;

import com.example.crud.constant.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tokens")
@FieldDefaults(level = AccessLevel.PUBLIC)
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String token;
    @Enumerated(EnumType.STRING)
    TokenType tokenType = TokenType.BEARER;
    boolean revoked;
    boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")

    UserEntity user;
}
