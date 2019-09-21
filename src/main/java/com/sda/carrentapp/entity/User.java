package com.sda.carrentapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"department"})
@EqualsAndHashCode(exclude = {"department"})
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String address;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    //    @Enumerated(value = EnumType.STRING)
    private EntityStatus entityStatus;
    private String username;
    private String password;

    @ManyToOne
    private Department department;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Booking> booking;

}
