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
public class DepartmentDTO {

    private Long id;

    private String name;

}
