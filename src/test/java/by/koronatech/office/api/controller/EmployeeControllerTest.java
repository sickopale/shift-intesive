package by.koronatech.office.api.controller;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeControllerTest {

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private Flyway flyway;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        flyway.migrate();
    }

    @Test
    void testGetEmployeesInDepartment() throws Exception {

        mockMvc.perform(get("/company/api/employee/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Анна Козлова"))
                .andExpect(jsonPath("$[1].name").value("Дмитрий Степанов"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        String newEmployeeJson = "{\"name\":\"Иван Иванов\",\"salary\":50000,\"departmentName\":\"Лудоманы\",\"isManager\":false}";

        mockMvc.perform(post("/company/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Иван Иванов"));
    }

    @Test
    void testPromoteEmployeeToManager() throws Exception {
        mockMvc.perform(patch("/company/api/employee/promote/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isManager").value(true));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        String updatedEmployeeJson = "{\"name\":\"Анна Козлова\",\"salary\":85000,\"departmentName\":\"Лудоманы\",\"isManager\":false}";

        mockMvc.perform(patch("/company/api/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(85000))
                .andExpect(jsonPath("$.departmentName").value("Лудоманы"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/company/api/employee/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee has been deleted"));
    }
}