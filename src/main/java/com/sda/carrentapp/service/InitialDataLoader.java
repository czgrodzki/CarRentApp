package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.*;
import com.sda.carrentapp.repository.BookingRepository;
import com.sda.carrentapp.repository.CarRepository;
import com.sda.carrentapp.repository.DepartmentRepository;
import com.sda.carrentapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class InitialDataLoader {
    private CarRepository carsRepository;
    private BookingRepository bookingRepository;
    private UserRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserService userServiceSecurity;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @EventListener
    @Transactional
    public void init(ApplicationReadyEvent applicationReadyEvent) {
        RentalOffice rentalOffice = new RentalOffice("C@rs", "www.rental.com", "Warsaw", "Marian Kowalski", "logo");

        Department department1 = Department.builder().address("Kraków").rentalOffice(rentalOffice).entityStatus(EntityStatus.ACTIVE).build();
        Department department2 = Department.builder().address("Gdańsk").rentalOffice(rentalOffice).entityStatus(EntityStatus.ACTIVE).build();
        Department department3 = Department.builder().address("Sopot").rentalOffice(rentalOffice).entityStatus(EntityStatus.ACTIVE).build();
        Department department4 = Department.builder().address("Warszawa").rentalOffice(rentalOffice).entityStatus(EntityStatus.ACTIVE).build();
        Department department5 = Department.builder().address("unsign").rentalOffice(rentalOffice).entityStatus(EntityStatus.ARCHIVED).build();

        List<Department> departments = new ArrayList<>(Arrays.asList(department1, department2, department3, department4, department5));
        departments.forEach(departmentRepository::save);

        Car car1 = Car.builder()
                .brand("Hyundai")
                .model("i30")
                .bodyType("hatchback")
                .productionYear("2010")
                .color("grey")
                .mileage(87_000d)
                .status(Status.AVAILABLE)
                .dailyFee(35.00)
                .department(department1)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Car car2 = Car.builder()
                .brand("Ford")
                .model("Fiesta")
                .bodyType("hatchback")
                .productionYear("2014")
                .color("blue")
                .mileage(36_500d)
                .status(Status.AVAILABLE)
                .dailyFee(20.00)
                .department(department1)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Car car3 = Car.builder()
                .brand("Ford")
                .model("Focus")
                .bodyType("hatchback")
                .productionYear("2012")
                .color("black")
                .mileage(40_800d)
                .status(Status.AVAILABLE)
                .dailyFee(30.00)
                .department(department2)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Car car4 = Car.builder()
                .brand("Hyundai")
                .model("i50")
                .bodyType("combi")
                .productionYear("2011")
                .color("grey")
                .mileage(83_200d)
                .status(Status.AVAILABLE)
                .dailyFee(39.00)
                .department(department2)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Car car5 = Car.builder()
                .brand("Fiat")
                .model("Multipla")
                .bodyType("VAN")
                .productionYear("2015")
                .color("grey")
                .mileage(87_000d)
                .status(Status.AVAILABLE)
                .dailyFee(49.00)
                .department(department3)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Car car6 = Car.builder()
                .brand("Audi")
                .model("Q5")
                .bodyType("SUV")
                .productionYear("2018")
                .color("silver")
                .mileage(25_300d)
                .status(Status.AVAILABLE)
                .dailyFee(59.00)
                .department(department3)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Car car7 = Car.builder()
                .brand("Audi")
                .model("Q7")
                .bodyType("SUV")
                .productionYear("2019")
                .color("white")
                .mileage(35_300d)
                .status(Status.AVAILABLE)
                .dailyFee(82.00)
                .department(department4)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Car car8 = Car.builder()
                .brand("Audi")
                .model("Q7")
                .bodyType("SUV")
                .productionYear("2016")
                .color("black")
                .mileage(55_300d)
                .status(Status.AVAILABLE)
                .dailyFee(62.00)
                .department(department4)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        List<Car> cars = new ArrayList<>(Arrays.asList(car1, car2, car3, car4, car5, car6, car7, car8));
        cars.forEach(carsRepository::save);

        User user1 = User.builder()
                .firstName("Adam")
                .lastName("Adamski")
                .email("aadamski@gmail.com")
                .role(Role.USER)
                .username("adam")
                .password(encoder.encode("adamski"))
                .address("Kraków")
                .department(department5)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        User user2 = User.builder()
                .firstName("Anna")
                .lastName("Nowak")
                .email("anowak@gmail.com")
                .role(Role.USER)
                .username("anna")
                .password(encoder.encode("nowak"))
                .address("Gdańsk")
                .department(department5)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        User user3 = User.builder()
                .firstName("Marta")
                .lastName("Kowalska")
                .role(Role.USER)
                .email("mkowalska@gmail.com")
                .username("marta")
                .password(encoder.encode("kowalska"))
                .address("Sopot")
                .department(department5)
                .entityStatus(EntityStatus.ACTIVE)
                .build();

        Booking booking1 = Booking.builder()
                .reservationDate(LocalDate.now().minusDays(10))
                .startDate(LocalDate.now().minusDays(2))
                .endDate(LocalDate.now().minusDays(1))
                .fee(200d)
                .rentDepartment(department1)
                .returnDepartment(department2)
                .user(user1)
                .car(car1)
                .entityStatus(EntityStatus.ARCHIVED)
                .build();

        Booking booking2 = Booking.builder()
                .reservationDate(LocalDate.now().minusDays(23))
                .startDate(LocalDate.now().minusDays(12))
                .endDate(LocalDate.now().minusDays(11))
                .fee(300d)
                .rentDepartment(department1)
                .returnDepartment(department3)
                .user(user2)
                .car(car2)
                .entityStatus(EntityStatus.ARCHIVED)
                .build();

        Booking booking3 = Booking.builder()
                .reservationDate(LocalDate.now().minusDays(13))
                .startDate(LocalDate.now().minusDays(14))
                .endDate(LocalDate.now().minusDays(10))
                .fee(400d)
                .rentDepartment(department2)
                .returnDepartment(department3)
                .user(user3)
                .car(car5)
                .entityStatus(EntityStatus.ARCHIVED)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);

        User employee1 = User.builder()
                .firstName("Jacek")
                .lastName("Her")
                .email("jh@email.com")
                .address("Gdańsk")
                .role(Role.ADMIN)
                .entityStatus(EntityStatus.ACTIVE)
                .username("jacek")
                .password(encoder.encode("jacek"))
                .department(department5)
                .build();

        User employee2 = User.builder()
                .firstName("Lukasz")
                .lastName("Mie")
                .email("lm@email.com")
                .address("Gdańsk")
                .role(Role.ADMIN)
                .entityStatus(EntityStatus.ACTIVE)
                .username("lukasz")
                .password(encoder.encode("lukasz"))
                .department(department5)
                .build();

        User employee3 = User.builder()
                .firstName("Michał")
                .lastName("Miler")
                .email("mm@email.com")
                .address("Kraków")
                .role(Role.MANAGER)
                .entityStatus(EntityStatus.ACTIVE)
                .username("michal")
                .password(encoder.encode("michal"))
                .department(department1)
                .build();

        User employee4 = User.builder()
                .firstName("Marcin")
                .lastName("Bacz")
                .email("mb@email.com")
                .address("Kraków")
                .role(Role.EMPLOYEE)
                .entityStatus(EntityStatus.ACTIVE)
                .username("marcin")
                .password(encoder.encode("marcin"))
                .department(department1)
                .build();

        User employee5 = User.builder()
                .firstName("Andrzej")
                .lastName("Pon")
                .email("ap@email.com")
                .address("Gdańsk")
                .role(Role.MANAGER)
                .entityStatus(EntityStatus.ACTIVE)
                .username("andrzej")
                .password(encoder.encode("andrzej"))
                .department(department2)
                .build();

        User employee6 = User.builder()
                .firstName("Szymon")
                .lastName("Nowak")
                .email("sn@email.com")
                .address("Gdańsk")
                .role(Role.EMPLOYEE)
                .entityStatus(EntityStatus.ACTIVE)
                .username("szymon")
                .password(encoder.encode("szymon"))
                .department(department2)
                .build();

        User employee7 = User.builder()
                .firstName("Monika")
                .lastName("Malek")
                .email("mm@email.com")
                .address("Sopot")
                .role(Role.MANAGER)
                .entityStatus(EntityStatus.ACTIVE)
                .username("monika")
                .password(encoder.encode("monika"))
                .department(department3)
                .build();

        User employee8 = User.builder()
                .firstName("Patryk")
                .lastName("Nowicki")
                .email("pn@email.com")
                .address("Sopot")
                .role(Role.EMPLOYEE)
                .entityStatus(EntityStatus.ACTIVE)
                .username("patryk")
                .password(encoder.encode("patryk"))
                .department(department3)
                .build();

        User employee9 = User.builder()
                .firstName("Paweł")
                .lastName("Kowalski")
                .email("pk@email.com")
                .address("Warszawa")
                .role(Role.MANAGER)
                .entityStatus(EntityStatus.ACTIVE)
                .username("pawel")
                .password(encoder.encode("pawel"))
                .department(department4)
                .build();

        User employee10 = User.builder()
                .firstName("Marian")
                .lastName("Kowalewski")
                .email("pn@email.com")
                .address("Warszawa")
                .role(Role.EMPLOYEE)
                .entityStatus(EntityStatus.ACTIVE)
                .username("marian")
                .password(encoder.encode("marian"))
                .department(department4)
                .build();

        List<User> employees = new ArrayList<>
                (Arrays.asList(employee1, employee2, employee3, employee4, employee5, employee6, employee7, employee8, employee9, employee10));

        employees.forEach(employeeRepository::save);
    }
}
