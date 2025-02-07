package by.koronatech.office.core.repository;

import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
    Optional<Department> findByName(String name);
    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    Page<Employee> findEmployeesByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);
}