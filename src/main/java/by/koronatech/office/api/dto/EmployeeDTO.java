package by.koronatech.office.api.dto;

import by.koronatech.office.core.entity.department.Department;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class EmployeeDTO {

    String name;

    Double salary;

    String department;

    Boolean manager;
}
