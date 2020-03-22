package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.dto.CarDto;
import com.sda.carrentapp.entity.mapper.CarMapper;
import com.sda.carrentapp.exception.CarNotFoundException;
import com.sda.carrentapp.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Test
    void getActiveCarsShouldReturnListOfActiveCars() {

        //given
        Car car = new Car();
        Car car2 = new Car();

        car.setEntityStatus(EntityStatus.ACTIVE);
        car2.setEntityStatus(EntityStatus.ACTIVE);

        List<Car> activeCarsMock = new ArrayList<>();
        activeCarsMock.add(car);
        activeCarsMock.add(car2);

        given(carRepository.findAllByEntityStatus(EntityStatus.ACTIVE)).willReturn(activeCarsMock);

        //when
        List<Car> activeCars = carService.getActiveCars();

        //then
        then(carRepository).should().findAllByEntityStatus(EntityStatus.ACTIVE);

        assertThat(activeCars.size(), equalTo(2));
        assertThat(activeCars.get(0), equalTo(car));
        assertThat(activeCars.get(1), equalTo(car2));
    }

    @Test
    void getActiveCarsShouldReturnEmptyList() {

        //given
        List<Car> activeCarsMock = new ArrayList<>();
        given(carRepository.findAllByEntityStatus(EntityStatus.ACTIVE)).willReturn(activeCarsMock);

        //when
        List<Car> activeCars = carService.getActiveCars();

        //then
        then(carRepository).should().findAllByEntityStatus(EntityStatus.ACTIVE);

        assertThat(activeCars, empty());

    }

    @Test
    void getCarsByRentDepAndDateAndStatusShouldReturnSetOf2CarsWithGivenData() {

        //given
        Car car = new Car();
        car.setColor("Red");
        Car car2 = new Car();
        car2.setColor("blue");

        Set<Car> cars = new HashSet<>();
        cars.add(car);
        cars.add(car2);

        LocalDate begin = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();

        Department department = new Department();

        given(carRepository.findAllByRentDepartmentAndDateAndStatus(begin, end, department)).willReturn(cars);

        //when
        Set<Car> carsResultSet = carService.getCarsByRentDepAndDateAndStatus(begin, end, department);

        //then
        assertThat(carsResultSet.size(), is(2));
        assertThat(carsResultSet.contains(car), is(true));
        assertThat(carsResultSet.contains(car2), is(true));

    }

    @Test
    void getCarsByRentDepAndDateAndStatusShouldReturnEmptySet() {

        //given
        Set<Car> cars = new HashSet<>();
        LocalDate begin = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();

        Department department = new Department();

        given(carRepository.findAllByRentDepartmentAndDateAndStatus(begin, end, department)).willReturn(cars);

        //when
        Set<Car> carsResultSet = carService.getCarsByRentDepAndDateAndStatus(begin, end, department);

        //then
        assertThat(carsResultSet, empty());
    }

    @Test
    void getCarByIdShouldThrowExceptionDueToNoCarWithGivenId() {

        //given
        //when
        //then
        assertThrows(CarNotFoundException.class, () -> carService.getCarById(1L));

    }

    @Test
    void getCarByIdShouldReturnCarWithGivenId() {

        //given
        Car car = new Car();
        given(carRepository.findById(1L)).willReturn(Optional.of(car));

        //when
        Car resultCar = carService.getCarById(1L);

        //then
        assertThat(resultCar, equalTo(car));

    }

    @Test
    void saveCarShouldChangeCarDTOStatusForActiveAndInvokeSaveMethod() {

        //given
        CarDto carDto = new CarDto();

        //when
        carService.saveCar(carDto);

        //then
        then(carRepository).should().save(CarMapper.toEntity(carDto));
        assertThat(carDto.getEntityStatus(), is(EntityStatus.ACTIVE));

    }

    @Test
    void deleteCarShouldThrowExceptionDueToNoCarWithGivenId() {

        //given
        //when
        //then
        assertThrows(CarNotFoundException.class, () -> carService.deleteCar(1L));

    }

    @Test
    void deleteCarShouldSetCarEntityStatusAsArchivedAndInvokeSaveMethod() {

        //given
        Car car = new Car();
        given(carRepository.findById(1L)).willReturn(Optional.of(car));

        //when
        carService.deleteCar(1L);

        //then
        then(carRepository).should().save(car);
        assertThat(car.getEntityStatus(), is(EntityStatus.ARCHIVED));
    }


}