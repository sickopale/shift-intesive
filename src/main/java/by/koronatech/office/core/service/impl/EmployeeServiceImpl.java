package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.employee.EmployeeMapper;
import by.koronatech.office.core.repository.EmployeeRepository;
import by.koronatech.office.core.service.DepartmentService;
import by.koronatech.office.core.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final EmployeeMapper employeeMapper;

    public Employee findById(long id) {
        log.info("Fetching employee by ID: {}", id);
        return employeeRepository.findById(id).orElseThrow(() -> {
            log.error("Employee with ID {} not found", id);
            return new ResourceNotFoundException("Employee with id " + id + " not found");
        });
    }

    @Override
    public EmployeeDTO promoteToManager(long id) {
        log.info("Promoting employee with ID {} to manager", id);
        Employee employee = findById(id);

        if (employee.getIsManager()) {
            log.warn("Employee with ID {} is already a manager", id);
            throw new IllegalArgumentException("The employee is already a manager");
        }

        employee.setIsManager(true);
        employeeRepository.save(employee);
        log.info("Employee with ID {} promoted to manager", id);

        return employeeMapper.toDto(employee);
    }

    @Override
    public Page<EmployeeDTO> getByDepartment(long id, Pageable pageable) {
        log.info("Fetching employees for department ID: {}", id);
        departmentService.findById(id);

        return employeeRepository.findEmployeesByDepartmentId(id, pageable)
                .map(employeeMapper::toDto);
    }

    @Override
    public EmployeeDTO updateEmployee(long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with ID: {}", id);
        Employee employee = findById(id);

        if (employeeDTO.getDepartmentName() != null) {
            if (!departmentService.checkDepartmentExist(employeeDTO.getDepartmentName())) {
                throw new IllegalArgumentException("The specified department does not exist");
            }
        }

        if ((employeeDTO.getSalary() != null) && (employeeDTO.getSalary() < 0)) {
            log.error("Incorrect salary value: {}", employeeDTO.getSalary());
            throw new IllegalArgumentException("Incorrect salary");
        }

        employee.setDepartment(departmentService.findByName(employeeDTO.getDepartmentName()));
        employeeRepository.save(employeeMapper.merge(employee, employeeDTO));

        log.info("Employee with ID {} successfully updated", id);
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating new employee");

        Employee newEmployee = employeeMapper.toEntity(employeeDTO);

        if (!departmentService.checkDepartmentExist(employeeDTO.getDepartmentName())) {
            throw new IllegalArgumentException("The specified department does not exist");
        }

        newEmployee.setDepartment(departmentService.findByName(employeeDTO.getDepartmentName()));
        employeeRepository.save(newEmployee);
        log.info("Employee '{}' successfully created", newEmployee.getName());

        return employeeMapper.toDto(newEmployee);
    }

    @Override
    public void deleteEmployeeById(long id) {
        log.info("Deleting employee with ID: {}", id);
        findById(id);
        employeeRepository.deleteById(id);
        log.info("Employee with ID {} successfully deleted", id);
    }
}

