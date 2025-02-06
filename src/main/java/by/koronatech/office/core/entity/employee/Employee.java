package by.koronatech.office.core.entity.employee;

import by.koronatech.office.core.entity.department.Department;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Employee {

    private Long id;

    private String name;

    private Double salary;
    // не было времени разбираться как сюда сам департамент а не строку засунуть(
    // может быть в сервис работников
    // тогда не пришлось бы инжектить департамент сервис и все было бы вообще красиво
    private String department;

    private Boolean isManager;

}
