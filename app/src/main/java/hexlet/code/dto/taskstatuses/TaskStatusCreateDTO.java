package hexlet.code.dto.taskstatuses;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusCreateDTO {

    @Size(min = 1)
    private String name;

    @Size(min = 1)
    private String slug;
}
