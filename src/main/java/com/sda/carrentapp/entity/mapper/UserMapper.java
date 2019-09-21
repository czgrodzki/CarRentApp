package com.sda.carrentapp.entity.mapper;

import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.entity.UserDTO;

public class UserMapper {
    public static User map(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setRole(userDTO.getRole());
        user.setEntityStatus(userDTO.getEntityStatus());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setDepartment(userDTO.getDepartment());
        user.setBooking(userDTO.getBooking());

        return user;
    }
}
