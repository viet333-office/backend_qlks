package com.example.crud.repository;

import com.example.crud.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity,Long> {
    @Query(
            value = """
			select t from TokenEntity t inner join UserEntity u\s
			on t.user.id = u.id\s
			where u.id = :id and (t.expired = false or t.revoked = false)\s
			""")
    List<TokenEntity> findAllValidTokenByUser(Long id);
    Optional<TokenEntity> findByToken (String token);
}
