package by.koronatech.office.core.service.impl;

import by.koronatech.office.api.dto.DepartmentDTO;
import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.department.DepartmentMapper;
import by.koronatech.office.core.repository.DepartmentRepository;
import by.koronatech.office.core.service.DepartmentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Getter
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public boolean checkDepartmentExist(String name) {
        log.info("Checking if department with name '{}' exists", name);
        boolean exists = departmentRepository.existsByName(name);
        log.info("Department with name '{}' exists: {}", name, exists);

        return exists;
    }

    public Department findById(long id) {
        log.info("Fetching department by ID: {}", id);
        return departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department with ID {} not found", id);
                    return new ResourceNotFoundException("Department with id " + id + " not found");
                });
    }

    public Department findByName(String name) {
        log.info("Fetching department by name: {}", name);
        return departmentRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("Department with name '{}' not found", name);
                    return new ResourceNotFoundException("Department with name " + name + " not found");
                });
    }

    @Override
    public Page<DepartmentDTO> getDepartments(Pageable pageable) {
        log.info("Fetching all departments with pageable: {}", pageable);
        return new PageImpl<>(departmentMapper.toDtos(departmentRepository.findAll(pageable)));
    }
}
