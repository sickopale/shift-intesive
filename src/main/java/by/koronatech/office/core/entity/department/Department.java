package by.koronatech.office.core.entity.department;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(exclude = "id")
public class Department {

    Long id;

    String name;

}
