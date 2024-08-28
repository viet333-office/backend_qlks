package com.example.crud.repository;

import com.example.crud.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;




@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("SELECT r FROM RoomEntity r WHERE "
            + "(:name IS NULL OR :name = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:room IS NULL OR :room = '' OR LOWER(r.room) LIKE LOWER(CONCAT('%', :room, '%'))) "
            + "AND (:value IS NULL OR CAST(r.value AS string) LIKE CONCAT('%', CAST(:value AS string), '%')) "
            + "AND (:status IS NULL OR :status = '' OR LOWER(r.status) LIKE LOWER(CONCAT('%', :status, '%'))) "
            + "AND (:stay IS NULL OR :stay = '' OR LOWER(r.stay) LIKE LOWER(CONCAT('%', :stay, '%')))"
    )

    Page<RoomEntity> searchByNameOrRoomOrValueOrStatusOrStay(
            @Param("name") String name,
            @Param("room") String room,
            @RequestParam("value") Long value,
            @RequestParam("status") String status,
            @RequestParam("stay") String stay,
            Pageable pageable
    );



    boolean existsByRoom(String room);
    boolean existsAllByRoom( String room );

}





