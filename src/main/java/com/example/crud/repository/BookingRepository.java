package com.example.crud.repository;

import com.example.crud.entity.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {


    @Query("SELECT b FROM BookingEntity b WHERE "
            + "(:start IS NULL  OR b.start >= :start) "
            + "AND (:end IS NULL  OR b.end <= :end) "
            + "AND (:id_customer IS NULL OR :id_customer = '' OR LOWER(b.id_customer) LIKE LOWER(CONCAT('%', :id_customer, '%'))) "
            + "AND (:phone_booking IS NULL OR :phone_booking = '' OR LOWER(b.phone_booking) LIKE LOWER(CONCAT('%', :phone_booking, '%')))"
            + "AND (:id_room IS NULL OR :id_room = '' OR LOWER(b.id_room) LIKE LOWER(CONCAT('%', :id_room, '%')))")
    Page<BookingEntity> searchByStartOrEndOrId_customerOrId_room(
            @RequestParam("start") Date start,
            @RequestParam("end") Date end,
            @RequestParam("id_customer") String id_customer,
            @RequestParam("phone_booking") String phone_booking,
            @RequestParam("id_room") String id_room,
            Pageable pageable
    );

    @Transactional
    @Modifying
    @Query("UPDATE BookingEntity b SET b.id_customer = :newCccd WHERE b.id_customer = :oldCccd")
    void updateBookingsCccd(@Param("oldCccd") String oldCccd, @Param("newCccd") String newCccd);

    @Transactional
    @Modifying
    @Query("UPDATE BookingEntity b SET b.phone_booking = :newPhone WHERE b.phone_booking = :oldPhone AND b.start >= CURRENT_DATE")
    void updateBookingsPhone(@Param("oldPhone") String oldPhone, @Param("newPhone") String newPhone);

    @Transactional
    @Modifying
    @Query("UPDATE BookingEntity b SET b.id_room = :newRoom WHERE b.id_room = :oldRoom")
    void updateBookingsRoom(@Param("oldRoom") String oldRoom, @Param("newRoom") String newRoom);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BookingEntity b WHERE b.id_room = :idRoom")
    Boolean existsByIdRoom(@Param("idRoom") String idRoom);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BookingEntity b WHERE b.id_customer = :idCustomer")
    Boolean existsByIdCustomer(@Param("idCustomer") String idCustomer);



    @Query("SELECT r.value FROM RoomEntity r WHERE r.room = :idRoom")
    Long findRoomValueByRoomId(@Param("idRoom") String idRoom);

    @Query("SELECT r.stay FROM RoomEntity r WHERE r.room = :idRoom")
    String findRoomStayByRoomId(@Param("idRoom") String idRoom);



    @Query("SELECT b FROM BookingEntity b WHERE b.id_room = :room AND b.start >= :currentDate")
    List<BookingEntity> findBookingsByRoomAndStartDateAfter(@Param("room") String room, @Param("currentDate") Date currentDate);

    @Modifying
    @Query("UPDATE BookingEntity b SET b.total = ((DATEDIFF(b.end, b.start) * (:newPrice / :stay))) WHERE b.id_room = :room AND b.start >= :currentDate")
    void updatePriceByRoomAndStartDateAfter(@Param("room") String room, @Param("newPrice") Long newPrice, @Param("stay") Long stay, @Param("currentDate") Date currentDate);


    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE b.id_room = :roomId AND " +
            "(:startDate = b.start OR " +
            ":endDate = b.end OR " +
            "(b.start BETWEEN :startDate AND :endDate) OR " +
            "(b.end BETWEEN :startDate AND :endDate) OR " +
            "(:startDate >= b.start AND :endDate <= b.end))")
    Long countConflictingBookings(@Param("roomId") String roomId,
                                  @Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate);
}

