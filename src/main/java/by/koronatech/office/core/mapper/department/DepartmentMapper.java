package by.koronatech.office.core.mapper.department;

import by.koronatech.office.api.dto.DepartmentDTO;
import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = BaseMapper.class)
public interface DepartmentMapper extends BaseMapper<Department, DepartmentDTO> {
}
