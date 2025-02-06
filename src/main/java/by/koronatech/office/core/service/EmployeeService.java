package by.koronatech.office.core.service;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    public Page<Employee> getEmployeesByDepartmentId(long id, Pageable pageable);

    public EmployeeDTO promoteToManager(long id);

    public EmployeeDTO updateEmployee(long id, EmployeeDTO employeeDTO);

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    public void deleteEmployeeById(long id);
}
