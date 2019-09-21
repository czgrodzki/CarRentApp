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

    //    @Query("select c from Car c where (c.) like lower(concat('%', :search, '%')) " +
//            "or lower(c.lastName) like lower(concat('%', :search, '%'))")
//    SELECT * FROM cars left join bookings on cars.id = bookings.car_id where cars.status = 'AVAILABLE' and cars.department_id = 1  and  bookings.end_date >= '2019-09-01'
//    @Query("select c from Car c left join c.bookings b where c.department=:department and c.status = 'AVAILABLE' and b.end_date > '2019-09-01' ")
//    @Query("select c from Car c left join c.bookings b where c.department_id = 1 and c.status = 'AVAILABLE' and b.end_date > '2019-09-01' ")

//    @Query("select c from cars c left join c.bookings b where " +
//            "c.department = :rentDepFromForm and " +
//            "c.status = 'AVAILABLE' " +
//            "and (b.endDate < :bookingStartDateFromForm or b.endDate = null)")

//SELECT cars.id FROM cars left join bookings on cars.id = bookings.car_id where cars.status = 'AVAILABLE' and cars.department_id = 1 or bookings.ENTITY_STATUS =0 and (bookings.end_date < '2019-09-28' and bookings.start_date < '2019-09-28' or bookings.start_date > '2019-09-24' and bookings.end_date > '2019-09-24')
//    select * from cars where  cars.department_id = 1 and  cars.id not in
//    (SELECT cars.id FROM cars left join bookings on cars.id = bookings.car_id where  '2019-09-08' between  bookings.start_date and bookings.end_date or  '2019-09-28' between  bookings.start_date and bookings.end_date)

//    @Query("select c from Car c where c.department = :rentDepFromForm and c.status = 'AVAILABLE' and c.id not in (select c.id from Car  left join c.bookings b where and (b.endDate < :bookingStartDateFromForm or b.endDate = null)) ")

    @Query(value = "select * from cars where  cars.department_id = :rentDepFromForm and  cars.id not in (SELECT cars.id FROM cars left join bookings on cars.id = bookings.car_id where  :bookingStartDateFromForm between  bookings.start_date and bookings.end_date or  :bookingEndDateFromForm between  bookings.start_date and bookings.end_date)", nativeQuery = true)
    Set<Car> findAllByRentDepartmentAndDateAndStatus(@Param("bookingStartDateFromForm") LocalDate startDate,
                                                     @Param("bookingEndDateFromForm") LocalDate endDate,
                                                     @Param("rentDepFromForm") Department rentDepFromForm);

    List<Car> findAllByDepartment(Department department);

}
