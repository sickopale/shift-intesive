package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.employee.EmployeeMapper;
import by.koronatech.office.core.repository.EmployeeRepository;
import by.koronatech.office.core.service.DepartmentService;
import by.koronatech.office.core.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private Employee employee2;
    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setId(1L);
        department.setName("Бухгалтерия");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("Анна Козлова");
        employee.setSalary(80000.0);
        employee.setDepartment(department);
        employee.setIsManager(false);

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Сергей Сидоров");
        employee2.setSalary(60000.0);
        employee2.setDepartment(department);
        employee2.setIsManager(false);
    }

    @Test
    void testFindById() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.findById(1L);
        assertEquals("Анна Козлова", foundEmployee.getName());
    }

    @Test
    void testPromoteToManager() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(any())).thenReturn(new EmployeeDTO(1L, "Анна Козлова", 80000.0, "Бухгалтерия", true));

        EmployeeDTO promotedEmployee = employeeService.promoteToManager(1L);
        assertTrue(promotedEmployee.getIsManager());
    }

    @Test
    void testGetByDepartment() {
        Page<Employee> employeePage = new PageImpl<>(Arrays.asList(employee, employee2));
        when(departmentService.findById(anyLong())).thenReturn(department);
        when(employeeRepository.findEmployeesByDepartmentId(anyLong(), any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toDto(any())).thenReturn(new EmployeeDTO(1L, "Анна Козлова", 80000.0, "Бухгалтерия", false));

        Page<EmployeeDTO> result = employeeService.getByDepartment(1L, Pageable.ofSize(5));
        assertEquals(2, result.getTotalElements());
        assertEquals("Анна Козлова", result.getContent().get(0).getName());
    }

    @Test
    void testUpdateEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "Анна Козлова", 85000.0, "Бухгалтерия", false);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(departmentService.checkDepartmentExist(any())).thenReturn(true);
        when(employeeMapper.merge(any(), any())).thenReturn(employee);
        when(employeeMapper.toDto(any())).thenReturn(employeeDTO);

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(1L, employeeDTO);
        assertEquals(85000.0, updatedEmployee.getSalary());
    }

    @Test
    void testCreateEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO(null, "Иван Иванов", 50000.0, "Бухгалтерия", false);

        when(departmentService.checkDepartmentExist(any())).thenReturn(true);
        when(departmentService.findByName(any())).thenReturn(department);

        when(employeeMapper.toEntity(any())).thenReturn(new Employee());
        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeMapper.toDto(any())).thenReturn(employeeDTO);

        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        assertEquals("Иван Иванов", createdEmployee.getName());
    }

    @Test
    void testDeleteEmployeeById() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        assertDoesNotThrow(() -> employeeService.deleteEmployeeById(1L));
    }

    @Test
    void testFindById_ThrowsException() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(2L));
        assertEquals("Employee with id 2 not found", exception.getMessage());
    }

    @Test
    void testPromoteToManager_ThrowsException() {
        employee.setIsManager(true);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> employeeService.promoteToManager(1L));
        assertEquals("The employee is already a manager", exception.getMessage());
    }

    @Test
    void testUpdateEmployeeThrowsException_IncorrectSalary() {
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "Анна Козлова", -1000.0, "Бухгалтерия", false);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(departmentService.checkDepartmentExist(any())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> employeeService.updateEmployee(1L, employeeDTO));
        assertEquals("Incorrect salary", exception.getMessage());
    }

    @Test
    void testCreateEmployee_ThrowsException_NoDepartment() {
        EmployeeDTO employeeDTO = new EmployeeDTO(null, "Иван Иванов", 50000.0, "Empty", false);
        when(departmentService.checkDepartmentExist(any())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(employeeDTO));
        assertEquals("The specified department does not exist", exception.getMessage());
    }

    @Test
    void testDeleteEmployeeById_ThrowsException() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployeeById(2L));
        assertEquals("Employee with id 2 not found", exception.getMessage());
    }
}