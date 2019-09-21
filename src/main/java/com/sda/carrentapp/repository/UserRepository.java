package com.sda.carrentapp.repository;

import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> findUserByUsername(String userName);

    List<User> findAllByEntityStatus(EntityStatus entityStatus);
}
