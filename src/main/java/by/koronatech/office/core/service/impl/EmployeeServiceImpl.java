package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.employee.EmployeeMapper;
import by.koronatech.office.core.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    //это нормально что я в этом сервисе юзаю другой, или лучше так не делать?
    private final DepartmentServiceImpl departmentService;
    private final EmployeeMapper employeeMapper;
    private final List<Employee> employees = new ArrayList<>(
            List.of(
                    Employee.builder().id(1L).name("Степан Рыбаков").salary(300.5).department("Маркетинг").isManager(false).build(),
                    Employee.builder().id(2L).name("Максим Козлов").salary(400.5).department("Маркетинг").isManager(false).build(),
                    Employee.builder().id(3L).name("Вероника Степанова").salary(700.5).department("Бухгалтерия").isManager(false).build()
            )
    );

    public Employee findById(long id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    //  Тут id тоже надо возвращать,
    // писать идентичную сущности дто не знаю правильно или нет,
    @Override
    public Page<Employee> getEmployeesByDepartmentId(long id, Pageable pageable) {

        Department department = departmentService.findById(id);

        List<Employee> employeesInDept = employees.stream()
                .filter(employee -> employee.getDepartment().equals(department.getName()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), employeesInDept.size());

        List<Employee> subList = employeesInDept.subList(start, end);

        return new PageImpl<>(subList, pageable, employeesInDept.size());
    }


    @Override
    public EmployeeDTO promoteToManager(long id) {
        Employee employee = findById(id);

        if (employee.getIsManager()) {
            throw new IllegalArgumentException("The employee is already a manager");
        }

        employee.setIsManager(true);
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(long id, EmployeeDTO employeeDTO) {
        Employee employee = findById(id);

        if (employeeDTO.getDepartment() != null) {
            if (!departmentService.checkDepartmentExist(employeeDTO.getDepartment())) {
                throw new IllegalArgumentException("The specified department does not exist");
            }
        }

        if ((employeeDTO.getSalary() != null) && (employeeDTO.getSalary() < 0))
            throw new IllegalArgumentException("Incorrect salary");

        employeeMapper.merge(employee, employeeDTO);
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {

        Employee newEmployee = employeeMapper.toEntity(employeeDTO);

        if (!departmentService.checkDepartmentExist(employeeDTO.getDepartment())) {
            throw new IllegalArgumentException("The specified department does not exist");
        }

        // здесь хз как умнее сделать, все равно нужно уникальный id ставить а мы тут с листом вынуждены работать

        long newId = employees.stream()
                .mapToLong(Employee::getId)
                .max()
                .orElse(0) + 1;

        newEmployee.setId(newId);

        employees.add(newEmployee);
        return employeeMapper.toDto(newEmployee);
    }

    @Override
    public void deleteEmployeeById(long id) {
        employees.remove(findById(id));
    }
}
