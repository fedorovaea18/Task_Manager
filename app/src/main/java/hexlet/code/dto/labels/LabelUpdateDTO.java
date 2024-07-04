package hexlet.code.dto.labels;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LabelUpdateDTO {

    @Size(min = 3, max = 1000)
    //@NotBlank
    private JsonNullable<String> name;
}
