package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.Role;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.entity.UserDTO;
import com.sda.carrentapp.entity.mapper.UserMapper;
import com.sda.carrentapp.exception.EmailsAreNotEqualException;
import com.sda.carrentapp.exception.PasswordsDoNotMatchException;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.exception.WrongOldPasswordException;
import com.sda.carrentapp.repository.DepartmentRepository;
import com.sda.carrentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder encoder;
    private UserRepository userRepository;
    private DepartmentRepository departmentRepository;

    @Autowired
    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAllByEntityStatus(EntityStatus.ACTIVE);
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User getUserByUserName(String userName) throws UserNotFoundException {
        return userRepository.findUserByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found with userName: " + userName));
    }

    public Optional<User> getOptionalUserByUserName(String userName) {
        return userRepository.findUserByUsername(userName);
    }

    public void saveUser(UserDTO userDTO) {
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        userDTO.setEntityStatus(EntityStatus.ACTIVE);
        departmentRepository.getDepartmentById(5L).ifPresent(userDTO::setDepartment);
        User userToSave = UserMapper.map(userDTO);
        userRepository.save(userToSave);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setEntityStatus(EntityStatus.ARCHIVED);
        userRepository.save(user);
    }

    public void changePassword(String userName, String oldPassword, String newPassword, String confirmPassword)
            throws UserNotFoundException, PasswordsDoNotMatchException, WrongOldPasswordException {
        User userToChangePassword = findUserToChangePassword(userName);

        checkIfPasswordEqualsConfirmPassword(newPassword, confirmPassword);
        if(!encoder.matches(oldPassword, userToChangePassword.getPassword())){
            throw new WrongOldPasswordException();
        }

        changedPasswordAndSave(newPassword, userToChangePassword);
    }

    public void restorePassword(String userName, String email, String password, String confirmPassword)
            throws UserNotFoundException, PasswordsDoNotMatchException, EmailsAreNotEqualException {
        User userToChangePassword = findUserToChangePassword(userName);

        checkIfPasswordEqualsConfirmPassword(password, confirmPassword);
        if(!userToChangePassword.getEmail().equals(email)){
            throw new EmailsAreNotEqualException();
        }

        changedPasswordAndSave(password, userToChangePassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username)
                .map(existingProfile -> new org.springframework.security.core.userdetails.User(
                        existingProfile.getUsername(),
                        existingProfile.getPassword(),
                        parseRoles(existingProfile.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Collection<? extends GrantedAuthority> parseRoles(Role roles) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roles.name()));

    }

    private User findUserToChangePassword(String userName) throws UserNotFoundException {
        return userRepository.findUserByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userName + " not found"));
    }

    private void checkIfPasswordEqualsConfirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }
    }

    private void changedPasswordAndSave(String newPassword, User userToChangePassword) {
        userToChangePassword.setPassword(encoder.encode(newPassword));
        userRepository.save(userToChangePassword);
    }
}
