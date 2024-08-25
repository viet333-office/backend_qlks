package com.example.crud.config.security;

import com.example.crud.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    /**
     * Bộ lọc xác thực JWT cho mỗi yêu cầu đến ứng dụng.
     *
     * @param request     Đối tượng HttpServletRequest chứa thông tin về yêu cầu
     * @param response    Đối tượng HttpServletResponse để trả về phản hồi
     * @param filterChain Chuỗi các bộ lọc để tiếp tục xử lý yêu cầu
     * @throws ServletException Ngoại lệ xảy ra khi xử lý yêu cầu
     * @throws IOException      Ngoại lệ xảy ra khi đọc/ghi dữ liệu
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
// bỏ qua yêu cầu đến đường dẫn /api/auth (dăng nhập,đăng ký,...)
//        if (request.getServletPath().contains("/api/v1/auth")){
//            filterChain.doFilter(request,response);
//            return;
//        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // kiểm tra xem header có null hoặc bắt đầu bằng chuỗi Bearer không
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        // nếu header tồn tại và đúng định dạng sẽ bỏ qua 7 ký tự đầu tiên
        jwt = authHeader.substring(7); // cắt chuỗi Header từ ký tự index số 7 trở đi ("Bearer")
        userEmail = jwtService.extractUsername(jwt); // lấy ra userEmail từ token

        // kiểm tra chuỗi vừa lấy ra có bị null hoặc ContextHoder có bị null
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            // kiểm tra xem token có hợp lệ hay không ,nếu hợp lệ lưu vào ContextHolder
            if (jwtService.isTokenValid(jwt,userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
filterChain.doFilter(request,response);


    }

}
