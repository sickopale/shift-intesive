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
    @GetMapping()
    public ResponseEntity<List<DepartmentDTO>> getDepartments(
            @PageableDefault(size = 5,page = 0,sort = "name") Pageable pageable) {
        return ResponseEntity.ok(departmentService.getDepartments(pageable).getContent());
    }

}
