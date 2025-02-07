package by.koronatech.office.api.dto;

import by.koronatech.office.core.entity.department.Department;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class EmployeeDTO {

    private String name;

    private Double salary;

    private String department;

    private Boolean isManager;
}
