package by.koronatech.office.api.controller;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company/api/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final String DEFAULT_SORT_FIELD = "name";

    @GetMapping("/departments/{departmentId}")
    public List<EmployeeDTO> getEmployeesInDepartment(
            @PathVariable long departmentId,
            @PageableDefault(size = DEFAULT_PAGE_SIZE, page = DEFAULT_PAGE_NUMBER, sort = DEFAULT_SORT_FIELD) Pageable pageable
    ) {
        return employeeService.getByDepartment(departmentId, pageable).getContent();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);
    }

    @PatchMapping("/promote/{employeeId}")
    public EmployeeDTO promoteEmployeeToManager(@PathVariable long employeeId) {
        return employeeService.promoteToManager(employeeId);
    }

    @PatchMapping("/{employeeId}")
    public EmployeeDTO updateEmployee(@PathVariable long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateEmployee(employeeId, employeeDTO);
    }

    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return "Employee has been deleted";
    }
}


