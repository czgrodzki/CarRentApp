package com.sda.carrentapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"users", "cars", "rentalOffice"})
@EqualsAndHashCode(exclude = {"users", "cars", "rentalOffice"})
@Entity(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    //    @Enumerated(value = EnumType.STRING)
    private EntityStatus entityStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "department")
    private Set<User> users;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "department")
    private Set<Car> cars;

    @ManyToOne(cascade = CascadeType.ALL)
    private RentalOffice rentalOffice;
}
