package com.example.crud.repository;

import com.example.crud.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("SELECT c FROM RoomEntity c WHERE "
            + "(:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:room IS NULL OR :room = '' OR c.room = :room) "
            + "AND (:value IS NULL OR c.value = :value) "
            + "AND (:status IS NULL OR :status = '' OR c.status = :status) "
            + "AND (:stay IS NULL OR :stay = '' OR c.stay = :stay)")
    Page<RoomEntity> filterRoom(
            @RequestParam("name") String name,
            @RequestParam("room") String room,
            @RequestParam("value") Long value,
            @RequestParam("status") String status,
            @RequestParam("stay") String stay,
            Pageable pageable
    );


    boolean existsById(Long id);
}





