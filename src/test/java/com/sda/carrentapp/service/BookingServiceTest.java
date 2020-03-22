package com.sda.carrentapp.service;


import com.sda.carrentapp.entity.*;
import com.sda.carrentapp.entity.mapper.BookingMapper;
import com.sda.carrentapp.exception.BookingNotFoundException;
import com.sda.carrentapp.exception.RentStartDateIsNullException;
import com.sda.carrentapp.repository.BookingRepository;
import com.sda.carrentapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldThrowAnExceptionDueToNoBookingWithGivenId() {

        //given
        Booking booking = new Booking();
        given(bookingRepository.getBookingById(1L)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(1L));
    }

    @Test
    void shouldReturnBookingWithID1() {

        //given
        Booking booking = new Booking();
        given(bookingRepository.getBookingById(1L)).willReturn(Optional.of(booking));

        //when
        Booking result = bookingService.getBookingById(1L);

        //then
        assertThat(result, is(booking));

    }

    @Test
    void shouldReturnListOf2BookingsFoundByGivenName() {

        //given
        Booking booking = new Booking();
        Booking booking2 = new Booking();

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        bookingList.add(booking2);

        given(bookingService.getAllBookingsByUserName("Jan Nowak")).willReturn(bookingList);

        //when
        List<Booking> resultList = bookingService.getAllBookingsByUserName("Jan Nowak");

        //then
        assertThat(resultList.size(), equalTo(2));
        assertThat(resultList.get(0), equalTo(booking));
        assertThat(resultList.get(1), equalTo(booking2));
    }

    @Test
    void shouldReturnEmptyListDueToNoBookingsForGivenName() {

        //given
        List<Booking> bookingList = new ArrayList<>();

        given(bookingService.getAllBookingsByUserName("Jan Nowak")).willReturn(bookingList);

        //when
        List<Booking> resultList = bookingService.getAllBookingsByUserName("Jan Nowak");

        //then
        assertThat(resultList.size(), equalTo(0));
        assertThat(resultList, empty());
    }

    @Test
    void shouldThrowExceptionDueToBookingWithGivenId() {

        //given
        given(bookingRepository.getBookingById(1L)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBooking(1L));
    }

    @Test
    void shouldSetBookingStatusAsArchivedDueToDeletion() {

        //given
        Booking booking = new Booking();
        given(bookingRepository.getBookingById(1L)).willReturn(Optional.of(booking));

        //when
        bookingService.deleteBooking(1L);

        //then
        then(bookingRepository).should().save(booking);
        assertThat(booking.getEntityStatus(), is(EntityStatus.ARCHIVED));

    }

    @Test
    void shouldSetLocalDateNowAndStatusAsActiveAndCarAndUserAndThanCallMethodSave() {

        //given
        UserBooking userBooking = new UserBooking();
        User user = new User();
        user.setUsername("Roman");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        given(securityContext.getAuthentication()).willReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn("Roman");
        given(userRepository.getUserByUsername(anyString())).willReturn(Optional.of(user));

        //when
        bookingService.addBooking(userBooking);

        //then
        then(bookingRepository).should().save(BookingMapper.toEntity(userBooking));

        assertThat(userBooking.getReservationDate(), equalTo(LocalDate.now()));
        assertThat(userBooking.getEntityStatus(), equalTo(EntityStatus.ACTIVE));
        assertThat(userBooking.getUser(), equalTo(user));

    }

    @Test
    void addNullBookingShouldThrowException() {

        //given
        //when
        //then
        assertThrows(BookingNotFoundException.class, () -> bookingService.addBooking(null));
    }

    @Test
    void acceptRentShouldThrowExceptionDueToNoBookingWithGivenId() {

        //given
        //when
        //then
        assertThrows(BookingNotFoundException.class, () -> bookingService.acceptRent(1L));
    }

    @Test
    void acceptRentShouldSetRentStarAndStatusAsRentAndCallSaveMethod() {

        //given
        Car car = new Car();
        Booking booking = new Booking();
        booking.setCar(car);
        given(bookingRepository.getBookingById(1L)).willReturn(Optional.of(booking));
        //when
        bookingService.acceptRent(1L);

        //then
        then(bookingRepository).should().save(booking);
        assertThat(booking.getRentStart().isAfter(LocalDateTime.now().minusSeconds(1)), is(true));
        assertThat(booking.getCar().getStatus(), equalTo(Status.RENT));
    }

    @Test
    void returnCarShouldThrowExceptionDueToNoBookingWithGivenID() {

        //given
        given(bookingRepository.getBookingById(1L)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(BookingNotFoundException.class, () -> bookingService.returnCar(1L));
    }

    @Test
    void returnCarShouldThrowExceptionDueToRentStartOfBookingBeingNull() {

        //given
        Booking booking = new Booking();
        given(bookingRepository.getBookingById(1L)).willReturn(Optional.of(booking));

        //when
        //then
        assertThrows(RentStartDateIsNullException.class, () -> bookingService.returnCar(1L));
    }

    @Test
    void returnCarShouldSetRentEndDateCarStatusAsAvailableCarDepartmentStatusOfBookingAsArchivedSetFeeAndInvokeSave() {

        //given
        Department department = new Department();
        Car car = new Car();
        car.setDailyFee(5.0);
        Booking booking = new Booking();
        booking.setRentStart(LocalDateTime.now().minusDays(1L));
        booking.setReturnDepartment(department);
        booking.setCar(car);

        given(bookingRepository.getBookingById(1L)).willReturn(Optional.of(booking));

        //when
        bookingService.returnCar(1L);

        //then
        then(bookingRepository).should().save(booking);
        assertThat(booking.getRentEnd().isAfter(LocalDateTime.now().minusSeconds(1)), is(true));
        assertThat(booking.getCar().getStatus(), equalTo(Status.AVAILABLE));
        assertThat(booking.getCar().getDepartment(), equalTo(department));
        assertThat(booking.getEntityStatus(), equalTo(EntityStatus.ARCHIVED));
        assertThat(booking.getFee(), anyOf(
                lessThan(100.0),
                greaterThan(50.0)
        ));
    }
}