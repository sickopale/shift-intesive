package by.koronatech.office.core.repository;

import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EmployeeRepositoryTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindEmployeesByDepartmentId() {

        Department department = new Department();
        department.setId(1L);
        department.setName("Бухгалтерия");

        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Анна Козлова");
        employee1.setSalary(80000.0);
        employee1.setDepartment(department);
        employee1.setIsManager(false);

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Сергей Сидоров");
        employee2.setSalary(60000.0);
        employee2.setDepartment(department);
        employee2.setIsManager(false);

        List<Employee> employees = Arrays.asList(employee1, employee2);
        Page<Employee> employeePage = new PageImpl<>(employees);

        when(employeeRepository.findEmployeesByDepartmentId(anyLong(), any(Pageable.class))).
                thenReturn(employeePage);

        Pageable pageable = Pageable.ofSize(5);
        Page<Employee> result = employeeRepository.findEmployeesByDepartmentId(1L, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Анна Козлова", result.getContent().get(0).getName());
        assertEquals("Сергей Сидоров", result.getContent().get(1).getName());
    }
}