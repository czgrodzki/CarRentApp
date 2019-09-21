package com.sda.carrentapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    @Column(name = "body_type")
    private String bodyType;
    @Column(name = "production_year")
    private String productionYear;
    private String color;
    private Double mileage;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private Double dailyFee;
    private EntityStatus entityStatus;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Department department;

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "car")
    private List<Booking> bookings;
}
