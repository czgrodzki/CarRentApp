package com.sda.carrentapp.entity.mapper;

import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.dto.CreateCarRequest;
import com.sda.carrentapp.entity.dto.CarDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarMapper {
    public static Car map(CreateCarRequest carRequest) {
        Car car = new Car();
        car.setBrand(carRequest.getBrand());
        car.setModel(carRequest.getModel());
        car.setBodyType(carRequest.getBodyType());
        car.setProductionYear(carRequest.getProductionYear());
        car.setColor(carRequest.getColor());
        car.setMileage(carRequest.getMileage());
        car.setStatus(carRequest.getStatus());
        car.setDailyFee(carRequest.getDailyFee());
        return car;
    }

    public static Car toEntity(CarDto carDto) {
        Car car = new Car();
        car.setId(carDto.getId());
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setBodyType(carDto.getBodyType());
        car.setProductionYear(carDto.getProductionYear());
        car.setColor(carDto.getColor());
        car.setMileage(carDto.getMileage());
        car.setStatus(carDto.getStatus());
        car.setDailyFee(carDto.getDailyFee());
        car.setEntityStatus(carDto.getEntityStatus());
        car.setDepartment(carDto.getDepartment());
        car.setBookings(carDto.getBookings());
        return car;
    }
}
