package com.sda.carrentapp.entity;

import lombok.Data;

@Data
public class CreateCarRequest {
    private String brand;
    private String model;
    private String bodyType;
    private String productionYear;
    private String color;
    private Double mileage;
    private Status status;
    private Double dailyFee;
    private EntityStatus entityStatus;
}
