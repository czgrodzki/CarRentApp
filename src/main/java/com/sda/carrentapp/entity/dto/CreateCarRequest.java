package com.sda.carrentapp.entity.dto;

import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.Status;
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
