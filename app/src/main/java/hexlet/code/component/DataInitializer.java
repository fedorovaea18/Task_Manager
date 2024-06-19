package hexlet.code.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import hexlet.code.model.User;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final CustomUserDetailsService userService;


    @Override
    public void run(ApplicationArguments args) {
        // Create default user
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);
    }
}
