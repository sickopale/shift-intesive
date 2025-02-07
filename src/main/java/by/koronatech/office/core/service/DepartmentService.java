package by.koronatech.office.core.service;

import by.koronatech.office.api.dto.DepartmentDTO;
import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.department.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    Page<DepartmentDTO> getDepartments(Pageable pageable);
    Department findById(long id);
    Department findByName(String name);
    boolean checkDepartmentExist(String name);
}
