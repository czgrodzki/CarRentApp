package com.sda.carrentapp.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Role role;
    private EntityStatus entityStatus;
    private String username;
    private String password;
    private Department department;
    private List<Booking> booking;

}
