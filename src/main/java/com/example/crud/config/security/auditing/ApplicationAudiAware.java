package com.example.crud.config.security.auditing;

import com.example.crud.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAudiAware implements AuditorAware<Long> {
    /**
     * Lấy thông tin về người dùng hiện tại đang xác thực trong ứng dụng.
     *
     * @return Optional chứa mã nhận dạng (ID) của người dùng nếu đang xác thực,
     * ngược lại trả về Optional.empty().
     */

    @Override
    public Optional<Long> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
        authentication instanceof AnonymousAuthenticationToken
        ){
            return Optional.empty();
        }
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        return Optional.ofNullable(userEntity.getId());
    }
}
