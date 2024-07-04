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
import java.util.List;
import java.util.Set;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
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
public class TaskControllerTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private JwtRequestPostProcessor token;

    private Task testTask;

    private User testUser;

    private Label testLabel;

    private TaskStatus testTaskStatus;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        userRepository.save(testUser);

        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
        labelRepository.save(testLabel);

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
        taskStatusRepository.save(testTaskStatus);

        testTask = Instancio.of(modelGenerator.getTaskModel())
                .create();
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        testTask.setLabels(Set.of(testLabel));
        taskRepository.save(testTask);
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetList() throws Exception {
        var request = get("/api/tasks")
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray().hasSize(1);
    }

    @Test
    public void testGetListWithParams() throws Exception {
        var request = get("/api/tasks?"
                + "titleCont=" + "Name"
                + "&assigneeId=" + 2024
                + "&status=" + "Slug"
                + "&labelId=" + 2024)
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray().hasSize(0);
    }

    @Test
    public void testCreate() throws Exception {
        var newUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(newUser);

        var newTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
        taskStatusRepository.save(newTaskStatus);

        var newLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
        labelRepository.save(newLabel);

        var newTask = Instancio.of(modelGenerator.getTaskModel())
                .create();

        var data = new TaskCreateDTO();
        data.setTitle(newTask.getName());
        data.setIndex(newTask.getIndex());
        data.setContent(newTask.getDescription());
        data.setStatus(newTaskStatus.getSlug());
        data.setAssigneeId(newUser.getId());
        data.setTaskLabelIds(List.of(newLabel.getId()));

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(newTask.getName()).get();

        assertNotNull(task);
        assertThat(task.getIndex()).isEqualTo(newTask.getIndex());
        assertThat(task.getDescription()).isEqualTo(newTask.getDescription());
        assertThat(task.getTaskStatus()).isEqualTo(newTaskStatus);
        assertThat(task.getAssignee()).isEqualTo(newUser);
        assertThat(task.getLabels()).isEqualTo(Set.of(newLabel));
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/tasks/" + testTask.getId())
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("id").isEqualTo(testTask.getId()),
                a -> a.node("title").isEqualTo(testTask.getName()),
                a -> a.node("index").isEqualTo(testTask.getIndex()),
                a -> a.node("content").isEqualTo(testTask.getDescription()),
                a -> a.node("status").isEqualTo(testTaskStatus.getSlug()),
                a -> a.node("assignee_id").isEqualTo(testUser.getId()),
                a -> a.node("createdAt").isEqualTo(testTask.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                a -> a.node("taskLabelIds").isEqualTo(List.of(testLabel.getId()))
        );
    }

    @Test
    public void testUpdate() throws Exception {

        var data = new HashMap<>();
        data.put("title", "NewTitle");
        data.put("index", 1);
        data.put("content", "NewContent");

        var request = put("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).get();

        assertThat(task.getName()).isEqualTo("NewTitle");
        assertThat(task.getIndex()).isEqualTo(1);
        assertThat(task.getDescription()).isEqualTo("NewContent");
    }

    @Test
    public void testDelete() throws Exception {
        var request = delete("/api/tasks/{id}", testTask.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);
    }
}
