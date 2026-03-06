package com.mps.birds.birds_ws.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.birds.birds_ws.dto.BirdDto;
import com.mps.birds.birds_ws.model.Bird;
import com.mps.birds.birds_ws.repository.BirdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class BirdsIntegrationTest {
	
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BirdRepository birdRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        birdRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveBird() throws Exception {
        BirdDto birdDto = BirdDto.builder()
            .name("Peregrine Falcon")
            .color("Grey")
            .weight(1.0)
            .height(0.5)
            .build();

        mockMvc.perform(post("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(birdDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Peregrine Falcon"));

        mockMvc.perform(get("/api/birds/names/Peregrine Falcon"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.color").value("Grey"));
    }

    @Test
    void shouldReturnBirdsOrderedByName() throws Exception {
        birdRepository.save(Bird.builder().name("Zebra Finch").color("Grey").weight(0.1).build());
        birdRepository.save(Bird.builder().name("Albatross").color("White").weight(8.0).build());

        mockMvc.perform(get("/api/birds"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Albatross"))
            .andExpect(jsonPath("$[1].name").value("Zebra Finch"));
    }

    @Test
    void shouldFilterBirdsByColor() throws Exception {
        birdRepository.save(Bird.builder().name("Blue Jay").color("Blue").weight(0.1).build());
        birdRepository.save(Bird.builder().name("Cardinal").color("Red").weight(0.1).build());

        mockMvc.perform(get("/api/birds/colors/Blue"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name").value("Blue Jay"));
    }

    @Test
    void shouldReturn404WhenBirdNotFound() throws Exception {
        mockMvc.perform(get("/api/birds/names/NonExistent"))
            .andExpect(status().isNotFound());
    }
}