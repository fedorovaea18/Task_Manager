package hexlet.code.dto.taskstatuses;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusCreateDTO {

    @Column(unique = true)
    @Size(min = 1)
    private String name;

    @Column(unique = true)
    @Size(min = 1)
    private String slug;
}
