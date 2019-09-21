package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.*;
import com.sda.carrentapp.entity.mapper.BookingMapper;
import com.sda.carrentapp.exception.BookingNotFoundException;
import com.sda.carrentapp.exception.RentStartDateIsNullException;
import com.sda.carrentapp.repository.BookingRepository;
import com.sda.carrentapp.repository.CarRepository;
import com.sda.carrentapp.repository.DepartmentRepository;
import com.sda.carrentapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private DepartmentRepository departmentRepository;
    private UserBooking userBooking;

    public BookingService(BookingRepository bookingRepository, CarRepository carRepository,
                          UserRepository userRepository, DepartmentRepository departmentRepository,
                          UserBooking userBooking) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.userBooking = userBooking;
    }

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) throws BookingNotFoundException {
        return bookingRepository.getBookingById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
    }

    public List<Booking> getAllBookingsByUserId(Long id) {
        return bookingRepository.findAllBookingsByUserId(id);
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
        userBooking.setReservationDate(LocalDate.now());
        userBooking.setEntityStatus(EntityStatus.ACTIVE);

        Optional<User> userByUsername = userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userBooking.setUser(userByUsername.get());

//        double fee = calculateFee(userBooking.getCar(), userBooking);
//        userBooking.setFee(fee);
        userBooking.setCar(userBooking.getCar());
//        userBooking.getCar().setStatus(Status.RENT);

//        userBooking.getCar().setDepartment(userBooking.getReturnDepartment());

        bookingRepository.save(BookingMapper.toEntity(userBooking));
    }

    public double calculateFee(Car selectedCar, UserBooking userBooking) {
        int days = Period.between(userBooking.getStartDate(), userBooking.getEndDate()).getDays();
        double fee = days * selectedCar.getDailyFee();

        if (!userBooking.getRentDepartment().equals(userBooking.getReturnDepartment())) {
            fee *= 1.25;
        }

        return fee;
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
        Long seconds = Duration.between(booking.getRentStart(), booking.getRentEnd()).getSeconds();
        Double days = Double.valueOf(seconds / (60 * 60 * 24));
        Double totalFee = Math.ceil(days) + booking.getCar().getDailyFee();
        return totalFee;
    }
}
