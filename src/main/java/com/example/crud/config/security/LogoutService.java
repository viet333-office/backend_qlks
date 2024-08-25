package com.example.crud.config.security;

import com.example.crud.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  LogoutService implements LogoutHandler {
    final TokenRepository tokenRepository;

    /**
     * Xử lý đăng xuất người dùng khỏi hệ thống.
     * Khi người dùng gửi yêu cầu đăng xuất, phương thức này sẽ được gọi.
     *
     * @param request        Đối tượng HttpServletRequest chứa thông tin về yêu cầu đăng xuất.
     * @param response       Đối tượng HttpServletResponse dùng để trả về phản hồi cho yêu cầu.
     * @param authentication Đối tượng Authentication chứa thông tin xác thực của người dùng.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return;
        }

        // nếu header tồn tại và đúng định dạng ,lấy chuỗi JWT từ sau cụm "Bearer"
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt) // Tìm token trong cơ sở dữ liệu  dựa trên chuỗi JWT
                .orElse(null);
        if (storedToken != null) {  // nếu token tồn tại trong cơ sở dữ liệu
            storedToken.setExpired(true);// đánh dấu token đã hết hạn
            storedToken.setRevoked(true);// đánh dấu token đã bị thu hồi
            tokenRepository.save(storedToken);// lưu lại trạng thái mới của token vào cơ sở dữ liệu
            SecurityContextHolder.clearContext(); // xóa thông tin xác thực khỏi Security
        }
    }
}
