package hexlet.code.dto.labels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LabelUpdateDTO {

    @Size(min = 3, max = 1000)
    //@NotNull
    @NotBlank
    private JsonNullable<String> name;
}
