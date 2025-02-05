package by.koronatech.office.core.service;

import by.koronatech.office.api.dto.EmployeeDTO;

import java.util.Map;

public interface CompanyService {

    public Map<String, Object> getDepartments(int page, int size);

    public Map<String, Object> getEmployeesByDepartmentId(long id, int page, int size);

    public void promoteToManager(long id);

    public void updateEmployee(long id, EmployeeDTO employeeDTO);

    public void createEmployee(EmployeeDTO employeeDTO);

    public void deleteEmployeeById(long id);
}
