package by.koronatech.office.core.service.impl;

import by.koronatech.office.core.entity.department.Department;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import by.koronatech.office.core.mapper.department.DepartmentMapper;
import by.koronatech.office.core.service.DepartmentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    private final List<Department> departments = new ArrayList<>(
            List.of(
                    Department.builder().id(1L).name("Бухгалтерия").build(),
                    Department.builder().id(2L).name("Маркетинг").build()
            )
    );

    public boolean checkDepartmentExist(String name) {
        return departments.stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(name));
    }

    public Department findById(long id) {
        return departments.stream()
                .filter(department -> department.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Department with id " + id + " not found"));
    }

    //Тут id тоже надо возвращать,
    // писать идентичную сущности дто не знаю правильно или нет,
    @Override
    public Page<Department> getDepartments(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), departments.size());

        List<Department> subList = departments.subList(start, end);
        return new PageImpl<>(subList, pageable, departments.size());
    }
}
