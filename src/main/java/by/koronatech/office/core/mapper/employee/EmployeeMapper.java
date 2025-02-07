package by.koronatech.office.core.mapper.employee;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = BaseMapper.class)
public interface EmployeeMapper extends BaseMapper<Employee, EmployeeDTO> {
    @Override
    @Mapping(source = "department.name", target = "departmentName")
    EmployeeDTO toDto(Employee employee);

    }
