package com.sda.carrentapp.entity.dto;

import com.sda.carrentapp.entity.Booking;
import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.Status;
import lombok.Data;

import java.util.List;

@Data
public class CarDto {
    private Long id;
    private String brand;
    private String model;
    private String bodyType;
    private String productionYear;
    private String color;
    private Double mileage;
    private Status status;
    private Double dailyFee;
    private EntityStatus entityStatus;
    private Department department;
    private List<Booking> bookings;
}
