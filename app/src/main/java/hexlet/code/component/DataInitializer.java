package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.util.LabelUtils;
import hexlet.code.util.TaskStatusUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CustomUserDetailsService userService;

    private final TaskStatusUtils taskStatusUtils;

    private final LabelUtils labelUtils;

    private final TaskStatusRepository taskStatusRepository;

    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);

        var defaultTaskStatuses = taskStatusUtils.getDefaultTaskStatuses();
        for (var status : defaultTaskStatuses) {
            taskStatusRepository.save(status);
        }

        var defaultLabels = labelUtils.getDefaultLabels();
        for (var label : defaultLabels) {
            if (labelRepository.findByName(label.getName()).isEmpty()) {
                labelRepository.save(label);
            }
        }
    }
}
