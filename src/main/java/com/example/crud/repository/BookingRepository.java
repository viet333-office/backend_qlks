package com.example.crud.repository;

import com.example.crud.entity.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("SELECT b FROM BookingEntity b")
    List<BookingEntity> findAll();


    @Query("SELECT b FROM BookingEntity b WHERE b.id_customer = :idCustomer")
    List<BookingEntity> findByCustomerId(@Param("idCustomer") Long idCustomer);

    @Query("SELECT b FROM BookingEntity b WHERE b.id_room = :idRoom")
    List<BookingEntity> findByRoomId(@Param("idRoom") Long idRoom);
    @Modifying
    @Query("DELETE FROM BookingEntity b WHERE b.id_customer = :idCustomer")
    void deleteByCustomerId(@Param("idCustomer") Long idCustomer);
    @Modifying
    @Query("DELETE FROM BookingEntity b WHERE b.id_room = :idRoom")
    void deleteByRoomId(@Param("idRoom") Long idRoom);




    @Query("SELECT b FROM BookingEntity b WHERE "
            + "(:start IS NULL OR b.start >= :start) AND "
            + "(:end IS NULL OR b.end <= :end) AND "
            + "(:id_customer IS NULL OR b.id_customer = :id_customer) AND "
            + "(:id_room IS NULL OR b.id_room = :id_room)")

    Page<BookingEntity> filter(
            @RequestParam("start")  Date start,
            @RequestParam("end")  Date end,
            @RequestParam("id_customer") Long id_customer,
            @RequestParam("id_room") Long id_room,
            Pageable pageable
    );
}

