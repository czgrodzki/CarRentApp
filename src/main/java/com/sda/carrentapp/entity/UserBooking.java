package com.sda.carrentapp.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
public class UserBooking {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Double fee;
    private EntityStatus entityStatus;
    private Department rentDepartment;
    private Department returnDepartment;
    private User user;
    private Car car;

}
