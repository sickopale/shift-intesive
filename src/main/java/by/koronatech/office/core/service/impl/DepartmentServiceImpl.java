package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.DepartmentDTO;
import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.department.DepartmentMapper;
import by.koronatech.office.core.repository.DepartmentRepository;
import by.koronatech.office.core.service.DepartmentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    public boolean checkDepartmentExist(String name) {

        return departmentRepository.existsByName(name);
    }

    public Department findById(long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id " + id + " not found"));
    }

    public Department findByName(String name){
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Department with name " + name + " not found"));
    }

    @Override
    public Page<DepartmentDTO> getDepartments(Pageable pageable) {
        return new PageImpl<>(departmentMapper.toDtos(departmentRepository.findAll(pageable)));
    }
}
