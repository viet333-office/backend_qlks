package com.example.crud.repository;

import com.example.crud.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {


    @Query("SELECT c FROM CustomerEntity c WHERE "
            + "(:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:phone IS NULL OR :phone = '' OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) "
            + "AND (:address IS NULL OR :address = '' OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) "
            + "AND (:cccd IS NULL OR :cccd = '' OR LOWER(c.cccd) LIKE LOWER(CONCAT('%', :cccd, '%')))"
    )
    Page<CustomerEntity> searchByNameOrAddressOrCccdOrPhone(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("address") String address,
            @Param("cccd") String cccd,
            Pageable pageable
    );


    boolean existsAllByCccd( String cccd );
    boolean existsByCccdAndIdNot(String cccd, Long id);

    boolean existsByCccd( String cccd);
    boolean existsByPhone( String phone);
}
