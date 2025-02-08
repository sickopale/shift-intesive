package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.DepartmentDTO;
import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.department.DepartmentMapper;
import by.koronatech.office.core.repository.DepartmentRepository;
import by.koronatech.office.core.service.impl.DepartmentServiceImpl;
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

public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private Department department2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setId(1L);
        department.setName("Бухгалтерия");

        department2 = new Department();
        department2.setId(2L);
        department2.setName("Маркетинг");
    }

    @Test
    void testCheckDepartmentExist() {
        when(departmentRepository.existsByName("Бухгалтерия")).thenReturn(true);
        when(departmentRepository.existsByName("Неизвестный")).thenReturn(false);

        assertTrue(departmentService.checkDepartmentExist("Бухгалтерия"));
        assertFalse(departmentService.checkDepartmentExist("Неизвестный"));
    }

    @Test
    void testFindById() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));

        Department foundDepartment = departmentService.findById(1L);
        assertEquals("Бухгалтерия", foundDepartment.getName());
        assertEquals(1L, foundDepartment.getId());
    }

    @Test
    void testFindByName() {
        when(departmentRepository.findByName("Бухгалтерия")).thenReturn(Optional.of(department));

        Department foundDepartment = departmentService.findByName("Бухгалтерия");
        assertEquals("Бухгалтерия", foundDepartment.getName());
        assertEquals(1L, foundDepartment.getId());
    }

    @Test
    void testGetDepartments() {
        Page<Department> departmentPage = new PageImpl<>(Arrays.asList(department, department2));

        when(departmentRepository.findAll(any(Pageable.class))).thenReturn(departmentPage);
        when(departmentMapper.toDtos(any())).thenReturn(Arrays.asList(new DepartmentDTO(1L, "Бухгалтерия"), new DepartmentDTO(2L, "Маркетинг")));

        Page<DepartmentDTO> result = departmentService.getDepartments(Pageable.ofSize(5));
        assertEquals(2, result.getTotalElements());
        assertEquals("Бухгалтерия", result.getContent().get(0).getName());
        assertEquals("Маркетинг", result.getContent().get(1).getName());
    }

    @Test
    void testFindById_ShouldThrowsException() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> departmentService.findById(2L));
        assertEquals("Department with id 2 not found", exception.getMessage());
    }

    @Test
    void testFindByName_ShouldThrowsException() {
        when(departmentRepository.findByName("Empty")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> departmentService.findByName("Empty"));
        assertEquals("Department with name Empty not found", exception.getMessage());
    }
}