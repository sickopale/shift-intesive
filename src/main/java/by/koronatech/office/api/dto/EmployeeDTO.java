package by.koronatech.office.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class EmployeeDTO {

    private Long id;

    private String name;

    private Double salary;

    private String departmentName;

    private Boolean isManager;
}
