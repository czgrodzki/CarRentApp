package com.sda.carrentapp.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"rentDepartment", "returnDepartment", "user", "car"})
@EqualsAndHashCode(exclude = {"rentDepartment", "returnDepartment", "user", "car"})
@Entity(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate reservationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime rentStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime rentEnd;
    private Double fee;
    private EntityStatus entityStatus = EntityStatus.ACTIVE;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Department rentDepartment;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Department returnDepartment;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Car car;
}
