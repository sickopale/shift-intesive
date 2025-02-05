package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.DepartmentDTO;
import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.department.DepartmentMapper;
import by.koronatech.office.core.mapper.employee.EmployeeMapper;
import by.koronatech.office.core.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;
    private final List<Department> departments = new ArrayList<>(
            List.of(
                    Department.builder().id(1L).name("Бухгалтерия").build(),
                    Department.builder().id(2L).name("Маркетинг").build()
            )
    );
    private final List<Employee> employees = new ArrayList<>(
            List.of(
                    Employee.builder().id(1L).name("Степан Рыбаков").salary(300.5).department("Маркетинг").manager(false).build(),
                    Employee.builder().id(2L).name("Максим Козлов").salary(400.5).department("Маркетинг").manager(false).build(),
                    Employee.builder().id(3L).name("Вероника Степанова").salary(700.5).department("Бухгалтерия").manager(false).build()
            )
    );

    public Map<String, Object> getDepartments(int page, int size) {
        List<DepartmentDTO> departmentDTOs = departmentMapper.toDtos(departments);
        return paginateList(departmentDTOs, page, size);
    }

    public Map<String, Object> getEmployeesByDepartmentId(long id, int page, int size) {
        Department department = departments.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Department with id " + id + " not found"));

        List<EmployeeDTO> employeesInDept = employees.stream()
                .filter(employee -> employee.getDepartment().equals(department.getName()))
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());

        return paginateList(employeesInDept, page, size);
    }

    public void promoteToManager(long id) {

        Employee employee = findById(id);

        if (employee.getManager()) {
            throw new IllegalArgumentException("The employee is already a manager");
        }

        employee.setManager(true);
    }

    public void updateEmployee(long id, EmployeeDTO employeeDTO) {
        Employee employee = findById(id);

        if (employeeDTO.getDepartment() != null) {
            if (!checkDepartmentExist(employeeDTO.getDepartment())) {
                throw new IllegalArgumentException("The specified department does not exist");
            }
        }

        if ((employeeDTO.getSalary() != null) && (employeeDTO.getSalary() < 0))
            throw new IllegalArgumentException("Incorrect salary");

        employeeMapper.merge(employee, employeeDTO);
        System.out.println(employee.toString());
    }

    public void createEmployee(EmployeeDTO employeeDTO) {

        Employee newEmployee = employeeMapper.toEntity(employeeDTO);

        if (employees.contains(newEmployee)) {
            throw new IllegalArgumentException("An employee with such data already exists");
        }

        if (!checkDepartmentExist(employeeDTO.getDepartment())) {
            throw new IllegalArgumentException("The specified department does not exist");
        }

        long newId = employees.stream()
                .mapToLong(Employee::getId)
                .max()
                .orElse(0) + 1;

        newEmployee.setId(newId);

        employees.add(newEmployee);
    }

    public void deleteEmployeeById(long id) {
        employees.remove(findById(id));
    }

    public Employee findById(long id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    public Boolean checkDepartmentExist(String name) {
        return departments.stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(name));
    }

    private <T> Map<String, Object> paginateList(List<T> list, int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, list.size());

        List<T> paginatedList = (start >= list.size()) ? Collections.emptyList() : list.subList(start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("data", paginatedList);
        response.put("totalItems", list.size());
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) list.size() / size));

        return response;
    }
}
