package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.*;
import com.sda.carrentapp.entity.mapper.BookingMapper;
import com.sda.carrentapp.exception.BookingNotFoundException;
import com.sda.carrentapp.exception.RentStartDateIsNullException;
import com.sda.carrentapp.repository.BookingRepository;
import com.sda.carrentapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;


    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) throws BookingNotFoundException {
        return bookingRepository.getBookingById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
    }


    public List<Booking> getAllBookingsByUserName(String userName) {
        return bookingRepository.findAllBookingsByUserName(userName);
    }

    public void deleteBooking(Long id) throws BookingNotFoundException {
        Booking booking = bookingRepository.getBookingById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
        booking.setEntityStatus(EntityStatus.ARCHIVED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void addBooking(UserBooking userBooking) {
        if (userBooking == null) {
            throw new BookingNotFoundException("Booking cannot be null");
        }

        userBooking.setReservationDate(LocalDate.now());
        userBooking.setEntityStatus(EntityStatus.ACTIVE);

        Optional<User> userByUsername = userRepository
                .getUserByUsername(SecurityContextHolder
                        .getContext().getAuthentication().getName());
        userBooking.setUser(userByUsername.get());
        userBooking.setCar(userBooking.getCar());
        bookingRepository.save(BookingMapper.toEntity(userBooking));
    }


    public void acceptRent(Long id) throws BookingNotFoundException {
        Booking bookingById = bookingRepository.getBookingById(id).orElseThrow(() -> new BookingNotFoundException(""));
        bookingById.setRentStart(LocalDateTime.now());
        bookingById.getCar().setStatus(Status.RENT);
        bookingRepository.save(bookingById);
    }

    public void returnCar(Long id) throws BookingNotFoundException, RentStartDateIsNullException {
        Booking bookingById = bookingRepository.getBookingById(id).orElseThrow(() -> new BookingNotFoundException(""));
        if(bookingById.getRentStart() == null){
            throw new RentStartDateIsNullException();
        }
        bookingById.setRentEnd(LocalDateTime.now());
        bookingById.getCar().setStatus(Status.AVAILABLE);
        bookingById.getCar().setDepartment(bookingById.getReturnDepartment());
        bookingById.setEntityStatus(EntityStatus.ARCHIVED);
        bookingById.setFee(totalFee(bookingById));
        bookingRepository.save(bookingById);
    }

    private Double totalFee(Booking booking) {
        long seconds = Duration.between(booking.getRentStart(), booking.getRentEnd()).getSeconds();
        double days = (double) (seconds / (60 * 60 * 24));
        return Math.ceil(days) + booking.getCar().getDailyFee();
    }
}
