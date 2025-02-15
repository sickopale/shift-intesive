package by.koronatech.office.core.repository;

import by.koronatech.office.core.entity.department.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DepartmentRepositoryTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByName() {

        when(departmentRepository.existsByName("Бухгалтерия")).thenReturn(true);
        when(departmentRepository.existsByName("Неизвестный")).thenReturn(false);

        assertTrue(departmentRepository.existsByName("Бухгалтерия"));
        assertFalse(departmentRepository.existsByName("Неизвестный"));

        verify(departmentRepository, times(1)).existsByName("Бухгалтерия");
        verify(departmentRepository, times(1)).existsByName("Неизвестный");
    }

    @Test
    void testFindByName() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Маркетинг");

        when(departmentRepository.findByName("Маркетинг")).thenReturn(Optional.of(department));
        when(departmentRepository.findByName("Неизвестный")).thenReturn(Optional.empty());

        Optional<Department> foundDepartment = departmentRepository.findByName("Маркетинг");
        assertTrue(foundDepartment.isPresent());
        assertTrue(foundDepartment.get().getName().equals("Маркетинг"));

        Optional<Department> unknownDepartment = departmentRepository.findByName("Неизвестный");
        assertFalse(unknownDepartment.isPresent());

        verify(departmentRepository, times(1)).findByName("Маркетинг");
        verify(departmentRepository, times(1)).findByName("Неизвестный");
    }
}