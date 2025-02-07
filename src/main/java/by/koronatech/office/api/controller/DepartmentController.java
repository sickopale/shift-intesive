package by.koronatech.office.api.controller;

import by.koronatech.office.api.dto.DepartmentDTO;
import by.koronatech.office.core.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/company/api/department")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final String DEFAULT_SORT_FIELD = "name";
    @GetMapping()
    public List<DepartmentDTO> getDepartments(
            @PageableDefault(size = DEFAULT_PAGE_SIZE,page = DEFAULT_PAGE_NUMBER,sort = "DEFAULT_SORT_FIELD") Pageable pageable) {
        return departmentService.getDepartments(pageable).getContent();
    }

}
