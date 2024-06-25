package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;

    private String title;

    private Integer index;

    private String content;

    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private LocalDate createdAt;
}
