package hexlet.code.dto.taskstatuses;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {

    @NotBlank
    @Column(unique = true)
    @Size(min = 1)
    private String name;

    @NotBlank
    //@Column(unique = true)
    //@Size(min = 1)
    private String slug;
}
