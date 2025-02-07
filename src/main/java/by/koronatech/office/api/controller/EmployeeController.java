package by.koronatech.office.api.controller;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.core.entity.employee.Employee;
import by.koronatech.office.core.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company/api/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{departmentId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesInDepartment(
            @PathVariable long departmentId,
            @PageableDefault(size = 5,page = 0,sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok(employeeService.getByDepartment(departmentId, pageable).getContent());
    }

    @PostMapping()
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeDTO));
    }

    @PatchMapping("/promote/{employeeId}")
    public ResponseEntity<EmployeeDTO> promoteEmployeeToManager(@PathVariable long employeeId) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.promoteToManager(employeeId));
    }

    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.updateEmployee(employeeId, employeeDTO));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.ok("Employee has been deleted");
    }
}

