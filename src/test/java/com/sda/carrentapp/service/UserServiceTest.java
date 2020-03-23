package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.repository.DepartmentRepository;
import com.sda.carrentapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;


    @Test
    void getUserByIdShouldThrowExceptionDueToNoUserWithGivenId() {

        //given
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));

    }

    @Test
    void getUserByIdShouldReturnUserWithGivenId() {

        //given
        User user = new User();

        given(userRepository.getUserById(anyLong())).willReturn(of(user));

        //when
        User userResult = userService.getUserById(23L);

        //then
        assertThat(userResult, is(user));
    }

    @Test
    void getUserByUserNameShouldThrowExceptionDueToNoUserWithGivenId() {

        //given
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUserName("Name"));

    }

    @Test
    void getUserByUserNameShouldReturnUserWithGivenId() {

        //given
        User user = new User();

        given(userRepository.findUserByUsername(anyString())).willReturn(of(user));

        //when
        User userResult = userService.getUserByUserName("Name");

        //then
        assertThat(userResult, is(user));
    }
}