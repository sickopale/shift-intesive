package by.koronatech.office.core.entity.employee;

import by.koronatech.office.core.entity.department.Department;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(exclude = "id")
public class Employee {

    Long id;

    String name;

    Double salary;

    String department;

    Boolean manager;

}
