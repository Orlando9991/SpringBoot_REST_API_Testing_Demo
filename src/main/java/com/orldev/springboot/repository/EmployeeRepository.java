package com.orldev.springboot.repository;

import com.orldev.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    //Define custom query using JPQL with index params
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQLIndex(String firstName, String lastName);

    //Define custom query using JPQL with named params
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName and e.lastName = :lastName")
    Employee findByJPQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //Define custom query using Native SQL with index params
    @Query(value = "SELECT * FROM employees e WHERE e.first_name = ?1 AND e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSQLIndex(String firstName, String lastName);

    //Define custom query using Native SQL with index params
    @Query(value = "SELECT * FROM employees e WHERE e.first_name = :firstName AND e.last_name = :lastName", nativeQuery = true)
    Employee findByNativeSQLNamed(@Param("firstName") String firstName,@Param("lastName") String lastName);
}
