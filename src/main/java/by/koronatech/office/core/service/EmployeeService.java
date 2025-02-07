package by.koronatech.office.core.service;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<EmployeeDTO> getByDepartment(long id, Pageable pageable);
    EmployeeDTO promoteToManager(long id);

    EmployeeDTO updateEmployee(long id, EmployeeDTO employeeDTO);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    void deleteEmployeeById(long id);
}
