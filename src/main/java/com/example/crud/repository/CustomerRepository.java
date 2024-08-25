package com.example.crud.repository;

import com.example.crud.entity.CustomerEntity;
import com.example.crud.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {



    @Query("SELECT c FROM CustomerEntity c")
    List<CustomerEntity> findAll();

    @Query("SELECT c FROM CustomerEntity c WHERE "
            + "(:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:phone IS NULL OR :phone = '' OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) "
            + "AND (:address IS NULL OR :address = '' OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) "
            + "AND (:cccd IS NULL OR :cccd = '' OR LOWER(c.cccd) LIKE LOWER(CONCAT('%', :cccd, '%')))"
    )
    List<CustomerEntity> searchByNameOrAddressOrCccdOrPhone(
            @RequestParam("name") String name,
            @RequestParam("address") String phone,
            @RequestParam("address") String address,
            @RequestParam("cccd") String cccd,
            Pageable pageable
    );




    boolean existsAllByCccd( String cccd );

    @Query("SELECT COUNT(c) > 0 FROM CustomerEntity c WHERE c.cccd = :cccd AND c.id <> :id")
    boolean existsAllByCccdAndIdNot(@Param("cccd") String cccd, @Param("id") Long id);

    boolean existsById(Long id);
}
