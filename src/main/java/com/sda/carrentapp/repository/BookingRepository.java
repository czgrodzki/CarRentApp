package com.sda.carrentapp.repository;

import com.sda.carrentapp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> getBookingById(Long id);

    @Query("select b from bookings b inner join b.user u where u.id = :id")
    List<Booking> findAllBookingsByUserId(@Param("id") Long id);

    @Query("select b from bookings b inner join b.user u where u.username = :username")
    List<Booking> findAllBookingsByUserName(@Param("username") String username);
}
