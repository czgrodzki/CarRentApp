package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void getEmployeeByIdShouldThrowExceptionDueToNoEmployeeWithGivenId() {

        //given
        //when
        //then
        assertThrows(UsernameNotFoundException.class, () -> employeeService.getEmployeeById(1L));

    }

    @Test
    void getEmployeeByIdShouldReturnEmployeeWithGivenId() {

        //given
        User user = new User();

        given(employeeRepository.findById(1L)).willReturn(of(user));

        //when
        User userResult = employeeService.getEmployeeById(1L);

        //then
        assertThat(userResult, is(user));

    }

    @Test
    void deleteShouldThrowExceptionDueToNoEmployeeWithGivenId() {

        //given
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> employeeService.delete(1L));

    }

    @Test
    void deleteShouldChangeEntityStatusForArchivedAndInvokeSaveMethod() {

        //given
        User user = new User();
        given(employeeRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        employeeService.delete(1L);

        //then
        assertThat(user.getEntityStatus(), is(EntityStatus.ARCHIVED));
        then(employeeRepository).should().save(user);

    }

}