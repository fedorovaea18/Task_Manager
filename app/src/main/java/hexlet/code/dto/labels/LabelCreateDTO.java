package hexlet.code.dto.labels;

import jakarta.persistence.Column;
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

    @Column(unique = true)
    @Size(min = 3, max = 1000)
    private String name;
}
