package com.example.crud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.crud.config.security.JwtService;
import com.example.crud.constant.TokenType;
import com.example.crud.dto.request.AuthenticationRequest;
import com.example.crud.dto.request.RegisterRequest;
import com.example.crud.dto.response.AuthenticationResponse;
import com.example.crud.entity.TokenEntity;
import com.example.crud.entity.UserEntity;
import com.example.crud.repository.TokenRepository;
import com.example.crud.repository.UserRepository;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {
    final UserRepository repository;
    final TokenRepository tokenRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;

    /**
     * Đăng ký người dùng mới vào hệ thống.
     *
     * @param registerRequest Đối tượng RegisterRequest chứa thông tin đăng ký
     * @return Đối tượng AuthenticationResponse chứa token truy cập và token làm mới
     */
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        // Tạo đối tượng User mới từ thông tin đăng ký
        var user = UserEntity.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        // Lưu đối tượng User vào cơ sở dữ liệu
        var savedUser = repository.save(user);

        // Tạo token truy cập và token làm mới cho người dùng
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Lưu token truy cập vào cơ sở dữ liệu
        saveUserToken(savedUser, jwtToken);

        // Trả về đối tượng AuthenticationResponse chứa các token
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Xác thực người dùng vào hệ thống.
     *
     * @param request Đối tượng AuthenticationRequest chứa thông tin xác thực
     * @return Đối tượng AuthenticationResponse chứa token truy cập và token làm mới
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Xác thực thông tin đăng nhập của người dùng
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        // Tìm đối tượng User tương ứng với email
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        // Tạo token truy cập và token làm mới cho người dùng
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Hủy tất cả các token hiện có của người dùng
        revokeAllUserTokens(user);

        // Lưu token truy cập mới vào cơ sở dữ liệu
        saveUserToken(user, jwtToken);

        // Trả về đối tượng AuthenticationResponse chứa các token
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Lưu token truy cập cho người dùng vào cơ sở dữ liệu.
     *
     * @param user     Đối tượng User
     * @param jwtToken Token truy cập JWT
     */
    private void saveUserToken(UserEntity user, String jwtToken) {

        // Tạo đối tượng Token mới
        var token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        // Lưu đối tượng Token vào cơ sở dữ liệu
        tokenRepository.save(token);
    }

    /**
     * Hủy tất cả các token hiện có của người dùng.
     *
     * @param user Đối tượng User
     */
    private void revokeAllUserTokens(UserEntity user) {

        // Tìm tất cả các token hợp lệ của người dùng
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;

        // Đánh dấu các token đó là hết hạn và bị hủy
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        // Lưu các token đã cập nhật vào cơ sở dữ liệu
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Làm mới token truy cập cho người dùng.
     *
     * @param request  Đối tượng HttpServletRequest
     * @param response Đối tượng HttpServletResponse
     * @throws IOException Ngoại lệ xảy ra khi ghi dữ liệu vào luồng đầu ra
     */
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        // Lấy token làm mới từ header yêu cầu
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);

        // Trích xuất email người dùng từ token làm mới
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            // Kiểm tra tính hợp lệ của token làm mới
            if (jwtService.isTokenValid(refreshToken, user)) {
                // Nếu token làm mới hợp lệ:
                //   - Tạo token truy cập mới cho người dùng
                //   - Hủy tất cả các token hiện có của người dùng
                //   - Lưu token truy cập mới vào cơ sở dữ liệu
                //   - Trả về đối tượng AuthenticationResponse chứa các token mới
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
