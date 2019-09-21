package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.dto.CarDto;
import com.sda.carrentapp.exception.CarNotFoundException;
import com.sda.carrentapp.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor

@Service
public class CarManager {
    private CarRepository carRepository;

    public List<Car> getActiveCars() {
        return carRepository.findAllByEntityStatus(EntityStatus.ACTIVE);
    }

    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public Set<Car> getCarsByRentDepAndDateAndStatus(LocalDate startDate, LocalDate endDate, Department rentDepartment) {
        return carRepository.findAllByRentDepartmentAndDateAndStatus(startDate, endDate, rentDepartment);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id).orElseThrow(CarNotFoundException::new);
    }

//    public void saveCar(CreateCarRequest carRequest) {
//        Car entity = CarMapper.map(carRequest);
//        entity.setEntityStatus(EntityStatus.ACTIVE);
//        carRepository.save(entity);
//    }

    public void saveCar(CarDto carDto) {
        carDto.setEntityStatus(EntityStatus.ACTIVE);
        Car car = CarMapper.toEntity(carDto);
        carRepository.save(car);
    }

    public void deleteCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setEntityStatus(EntityStatus.ARCHIVED);
        carRepository.save(car);
    }

//    public void patchCar(Long id, UpdateCarRequest updateCarRequest) {
//        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
//
//        Optional.ofNullable(updateCarRequest.getBrand()).ifPresent(car::setBrand);
//        Optional.ofNullable(updateCarRequest.getModel()).ifPresent(car::setModel);
//        Optional.ofNullable(updateCarRequest.getBodyType()).ifPresent(car::setBodyType);
//        Optional.ofNullable(updateCarRequest.getProductionYear()).ifPresent(car::setProductionYear);
//        Optional.ofNullable(updateCarRequest.getColor()).ifPresent(car::setColor);
//        Optional.ofNullable(updateCarRequest.getMileage()).ifPresent(car::setMileage);
//        Optional.ofNullable(updateCarRequest.getStatus()).ifPresent(car::setStatus);
//        Optional.ofNullable(updateCarRequest.getDailyFee()).ifPresent(car::setDailyFee);
//
//        carRepository.save(car);
//    }

//    public List<String> getAllBodyTypes() {
//        return carRepository.findAll().stream()
//                .map(Car::getBodyType)
//                .distinct()
//                .collect(Collectors.toList());
//    }
}
