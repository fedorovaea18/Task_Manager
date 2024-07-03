package hexlet.code.dto.labels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelCreateDTO {

    @Size(min = 3, max = 1000)
    //@NotNull
    @NotBlank
    private String name;
}
