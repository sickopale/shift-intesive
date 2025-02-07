package by.koronatech.office.api.dto;

import by.koronatech.office.core.entity.department.Department;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class EmployeeDTO {

    private Long id;

    private String name;

    private Double salary;

    private String departmentName;

    private Boolean isManager;
}
