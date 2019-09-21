package com.sda.carrentapp.entity.mapper;

import com.sda.carrentapp.entity.Booking;
import com.sda.carrentapp.entity.UserBooking;

public class BookingMapper {

    public static Booking toEntity(UserBooking userBooking) {
        Booking booking = new Booking();
        booking.setReservationDate(userBooking.getReservationDate());
        booking.setStartDate(userBooking.getStartDate());
        booking.setEndDate(userBooking.getEndDate());
        booking.setFee(userBooking.getFee());
        booking.setEntityStatus(userBooking.getEntityStatus());
        booking.setRentDepartment(userBooking.getRentDepartment());
        booking.setReturnDepartment(userBooking.getReturnDepartment());
        booking.setUser(userBooking.getUser());
        booking.setCar(userBooking.getCar());
        return booking;
    }
}
