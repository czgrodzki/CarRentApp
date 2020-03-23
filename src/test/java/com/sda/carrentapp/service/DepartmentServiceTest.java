package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.exception.DepartmentNotFoundException;
import com.sda.carrentapp.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    void getDepartmentsShouldReturnListOfDepartments() {

        //given
        Department department = new Department();
        Department department2 = new Department();

        List<Department> departments = new ArrayList<>();
        departments.add(department);
        departments.add(department2);

        given(departmentRepository.findAllByEntityStatus(EntityStatus.ACTIVE)).willReturn(departments);

        //when
        List<Department> departmentsResultList = departmentService.getDepartments();

        //then
        assertThat(departmentsResultList.size(), is(2));
        assertThat(departmentsResultList.get(0), is(department));
        assertThat(departmentsResultList.get(1), is(department2));
    }

    @Test
    void getDepartmentByIdShouldThrowExceptionDueToNoDepartmentWithGivenId() {

        //given
        //when
        //then
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentById(1L));

    }

    @Test
    void getDepartmentByIdShouldReturnDepartmentWithGivenId() {

        //given
        Department department = new Department();
        given(departmentRepository.getDepartmentById(1L)).willReturn(Optional.of(department));

        //when
        Department departmentById = departmentService.getDepartmentById(1L);

        //then
        assertThat(departmentById, is(department));

    }

    @Test
    void saveDepartmentShouldSetStatusAsActiveInGivenDepartmentAndInvokeSaveMethod() {

        //given
        Department department = new Department();

        //when
        departmentService.saveDepartment(department);

        //then
        assertThat(department.getEntityStatus(), is(EntityStatus.ACTIVE));
        then(departmentRepository).should().save(department);
    }

    @Test
    void deleteDepartmentShouldThrowExceptionDueToNoDepartmentWithGivenId() {

        //given
        //when
        //then
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.deleteDepartment(1L));
    }

    @Test
    void deleteDepartmentShouldShouldSetStatusAsArchivedAndInvokeSaveMethod() {

        //given
        Department department = new Department();
        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));

        //when
        departmentService.deleteDepartment(1L);

        //then
        then(departmentRepository).should().save(department);
        assertThat(department.getEntityStatus(), is(EntityStatus.ARCHIVED));

    }

    @Test
    void getEmployeesForDepartmentShouldThrowExceptionDueToNoDepartmentWithGivenId() {

        //given
        //when
        //then
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getEmployeesForDepartment(1L));

    }

    @Test
    void getEmployeesForDepartmentShouldReturnListOfActiveEmployeesById() {

        //given
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();
        User user5 = new User();
        User user6 = new User();

        user1.setId(3L);
        user2.setId(2L);
        user3.setId(1L);
        user4.setId(5L);
        user5.setId(4L);
        user6.setId(6L);

        user1.setEntityStatus(EntityStatus.ACTIVE);
        user2.setEntityStatus(EntityStatus.ACTIVE);
        user3.setEntityStatus(EntityStatus.ARCHIVED);
        user4.setEntityStatus(EntityStatus.ACTIVE);
        user5.setEntityStatus(EntityStatus.ARCHIVED);
        user6.setEntityStatus(EntityStatus.ACTIVE);

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);

        Department department = new Department();
        department.setUsers(users);

        given(departmentRepository.getDepartmentById(1L)).willReturn(Optional.of(department));

        //when
        List<User> usersResult = departmentService.getEmployeesForDepartment(1L);

        //then
        assertThat(usersResult.size(), is(4));
        assertThat(usersResult.get(0), is(user2));
        assertThat(usersResult.get(1), is(user1));
        assertThat(usersResult.get(2), is(user4));
        assertThat(usersResult.get(3), is(user6));

    }

    @Test
    void getCarsForDepartmentShouldReturnSortedListOfCarsForDepartmentWithGivenId() {

        //given
        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();
        Car car4 = new Car();
        Car car5 = new Car();
        Car car6 = new Car();

        car1.setId(3L);
        car2.setId(2L);
        car3.setId(1L);
        car4.setId(5L);
        car5.setId(4L);
        car6.setId(6L);

        Set<Car> cars = new HashSet<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        cars.add(car4);
        cars.add(car5);
        cars.add(car6);

        Department department = new Department();
        department.setCars(cars);

        given(departmentRepository.getDepartmentById(1L)).willReturn(Optional.of(department));

        //when
        List<Car> carResult = departmentService.getCarsForDepartment(1L);

        //then
        assertThat(carResult.size(), is(6));
        assertThat(carResult.get(0), is(car3));
        assertThat(carResult.get(1), is(car2));
        assertThat(carResult.get(2), is(car1));
        assertThat(carResult.get(3), is(car5));
        assertThat(carResult.get(4), is(car4));
        assertThat(carResult.get(5), is(car6));
    }

    @Test
    void getCarsForDepartmentShouldThrowExceptionDueToNoDepartmentWithGivenId() {

        //given
        //when
        //then
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getCarsForDepartment(1L));

    }

}