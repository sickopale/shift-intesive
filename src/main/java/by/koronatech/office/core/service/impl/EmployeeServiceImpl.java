package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.employee.EmployeeMapper;
import by.koronatech.office.core.repository.EmployeeRepository;
import by.koronatech.office.core.service.DepartmentService;
import by.koronatech.office.core.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final EmployeeMapper employeeMapper;

    public Employee findById(long id) {
        return employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    @Override
    public EmployeeDTO promoteToManager(long id) {
        Employee employee = findById(id);

        if (employee.getIsManager()) {
            throw new IllegalArgumentException("The employee is already a manager");
        }

        employee.setIsManager(true);
        employeeRepository.save(employee);

        return employeeMapper.toDto(employee);
    }

    @Override
    public Page<EmployeeDTO> getByDepartment(long id, Pageable pageable) {
        departmentService.findById(id);

        return employeeRepository.findEmployeesByDepartmentId(id, pageable)
                .map(employeeMapper::toDto);
    }
    @Override
    public EmployeeDTO updateEmployee(long id, EmployeeDTO employeeDTO) {
        Employee employee = findById(id);

        if (employeeDTO.getDepartmentName() != null) {
            if (!departmentService.checkDepartmentExist(employeeDTO.getDepartmentName())) {
                throw new IllegalArgumentException("The specified department does not exist");
            }
        }

        if ((employeeDTO.getSalary() != null) && (employeeDTO.getSalary() < 0))
            throw new IllegalArgumentException("Incorrect salary");

        employee.setDepartment(departmentService.findByName(employeeDTO.getDepartmentName()));

        employeeRepository.save(employeeMapper.merge(employee, employeeDTO));

        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {

        Employee newEmployee = employeeMapper.toEntity(employeeDTO);

        if (!departmentService.checkDepartmentExist(employeeDTO.getDepartmentName())) {
            throw new IllegalArgumentException("The specified department does not exist");
        }

        newEmployee.setDepartment(departmentService.findByName(employeeDTO.getDepartmentName()));

        employeeRepository.save(newEmployee);
        return employeeMapper.toDto(newEmployee);
    }

    @Override
    public void deleteEmployeeById(long id) {
        findById(id);
        employeeRepository.deleteById(id);
    }
}
