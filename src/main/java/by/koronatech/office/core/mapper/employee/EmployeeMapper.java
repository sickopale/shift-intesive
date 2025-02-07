package by.koronatech.office.core.mapper.employee;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", config = BaseMapper.class)
public interface EmployeeMapper extends BaseMapper<Employee, EmployeeDTO> {
}
