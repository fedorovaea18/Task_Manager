package hexlet.code.dto.taskstatuses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO {

    @NotBlank
    //@Column(unique = true)
    @Size(min = 1)
    private JsonNullable<String> name;

    @NotBlank
    //@Column(unique = true)
    @Size(min = 1)
    private JsonNullable<String> slug;
}
