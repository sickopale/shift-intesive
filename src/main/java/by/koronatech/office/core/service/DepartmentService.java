package by.koronatech.office.core.service;

import by.koronatech.office.core.entity.department.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    public Page<Department> getDepartments(Pageable pageable);
}
