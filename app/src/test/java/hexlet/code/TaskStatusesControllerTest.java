package hexlet.code;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import hexlet.code.dto.taskstatuses.TaskStatusCreateDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.util.ModelGenerator;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusesControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private JwtRequestPostProcessor token;

    private TaskStatus testTaskStatus;

    private User testUser;


    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        userRepository.save(testUser);

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
        taskStatusRepository.save(testTaskStatus);
    }

    @AfterEach
    public void clean() {
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        var newTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();

        var data = new TaskStatusCreateDTO();

        data.setName(newTaskStatus.getName());
        data.setSlug(newTaskStatus.getSlug());

        var request = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var taskStatus = taskStatusRepository.findBySlug(data.getSlug()).get();

        assertNotNull(taskStatus);
        assertThat(taskStatus.getName()).isEqualTo(data.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(data.getSlug());
    }

    @Test
    public void testCreateWithoutAuthorization() throws Exception {
        var newTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();

        var request = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newTaskStatus));

        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());

        assertThat(taskStatusRepository.findBySlug(newTaskStatus.getSlug())).isEmpty();
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/task_statuses/" + testTaskStatus.getId())
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                a -> a.node("id").isEqualTo(testTaskStatus.getId()),
                a -> a.node("name").isEqualTo(testTaskStatus.getName()),
                a -> a.node("slug").isEqualTo(testTaskStatus.getSlug()),
                a -> a.node("createdAt").isEqualTo(testTaskStatus.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        );
    }

    @Test
    public void testUpdate() throws Exception {

        var data = new HashMap<>();
        data.put("slug", "newSlug");

        var request = put("/api/task_statuses/" + testTaskStatus.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatus = taskStatusRepository.findById(testTaskStatus.getId()).get();

        assertThat(taskStatus.getSlug()).isEqualTo("newSlug");
        assertThat(taskStatusRepository.findBySlug(taskStatus.getSlug())).get().isEqualTo(taskStatus);
    }

    @Test
    public void testDelete() throws Exception {
        var request = delete("/api/task_statuses/{id}", testTaskStatus.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.existsById(testTaskStatus.getId())).isEqualTo(false);
    }
}
