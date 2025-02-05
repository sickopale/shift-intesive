package by.koronatech.office.api.controller;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.service.impl.CompanyServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/company/api")
@AllArgsConstructor
public class CompanyController {

    private final CompanyServiceImpl employeeService;

    @GetMapping("/employee/{departmentId}")
    public ResponseEntity<Map<String, Object>> getEmployeesInDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentId(departmentId, page, size));
    }

    @GetMapping("/department")
    public ResponseEntity<Map<String, Object>> getDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ) {
        return ResponseEntity.ok(employeeService.getDepartments(page, size));
    }

    @PostMapping("/employee")
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee has been created");
    }

    @PatchMapping("/employee/manager/{employeeId}")
    public ResponseEntity<String> promoteEmployeeToManager(@PathVariable Long employeeId) {
        employeeService.promoteToManager(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body("The employee has been promoted");
    }

    @PatchMapping("/employee/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeId, employeeDTO);
        return ResponseEntity.status(HttpStatus.OK).body("The employee has been updated");
    }

    @DeleteMapping("/employee/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.ok("Employee has been deleted");
    }

}