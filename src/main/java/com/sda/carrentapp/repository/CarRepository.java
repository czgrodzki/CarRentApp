package com.sda.carrentapp.repository;

import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByEntityStatus(EntityStatus entityStatus);

    @Query(value = "SELECT * FROM cars WHERE " +
            "cars.department_id = :rentDepFromForm AND cars.id NOT IN " +
            "(SELECT cars.id FROM cars LEFT JOIN bookings ON cars.id = bookings.car_id WHERE " +
            ":bookingStartDateFromForm BETWEEN bookings.start_date AND bookings.end_date ON " +
            ":bookingEndDateFromForm BETWEEN bookings.start_date AND bookings.end_date)", nativeQuery = true)
    Set<Car> findAllByRentDepartmentAndDateAndStatus(@Param("bookingStartDateFromForm") LocalDate startDate,
                                                     @Param("bookingEndDateFromForm") LocalDate endDate,
                                                     @Param("rentDepFromForm") Department rentDepFromForm);


}
