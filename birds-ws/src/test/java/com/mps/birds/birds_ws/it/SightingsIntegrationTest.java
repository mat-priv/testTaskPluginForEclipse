package com.mps.birds.birds_ws.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.birds.birds_ws.dto.SightingDto;
import com.mps.birds.birds_ws.model.Bird;
import com.mps.birds.birds_ws.model.Sighting;
import com.mps.birds.birds_ws.repository.BirdRepository;
import com.mps.birds.birds_ws.repository.SightingsRepository;
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

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SightingsIntegrationTest {
	
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
    private SightingsRepository sightingsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        sightingsRepository.deleteAll();
        birdRepository.deleteAll();
    }

    @Test
    void shouldCreateSightingForExistingBird() throws Exception {
        Bird bird = Bird.builder()
            .name("Red Robin")
            .color("Red")
            .weight(0.1)
            .build();
        birdRepository.save(bird);

        SightingDto sightingDto = SightingDto.builder()
            .birdName("Red Robin")
            .location("Central Park")
            .date(LocalDateTime.now())
            .build();

        mockMvc.perform(post("/api/sightings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sightingDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.location").value("Central Park"));

        mockMvc.perform(get("/api/sightings/by-bird-name")
                .param("name", "Red Robin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].location").value("Central Park"));
    }

    @Test
    void shouldFailToCreateSightingForMissingBird() throws Exception {
        SightingDto sightingDto = SightingDto.builder()
            .birdName("Invisible Bird")
            .location("Nowhere")
            .date(LocalDateTime.now())
            .build();

        mockMvc.perform(post("/api/sightings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sightingDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldFilterSightingsByTimeInterval() throws Exception {
        Bird bird = birdRepository.save(Bird.builder().name("Hawk").color("Brown").weight(1.5).build());

        sightingsRepository.save(Sighting.builder()
            .bird(bird).location("Forest").date(LocalDateTime.of(2023, 5, 10, 10, 0)).build());

        sightingsRepository.save(Sighting.builder()
            .bird(bird).location("Mountain").date(LocalDateTime.of(2023, 6, 10, 10, 0)).build());

        mockMvc.perform(get("/api/sightings/by-time")
                .param("start", "2023-05-01T00:00:00")
                .param("end", "2023-05-31T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].location").value("Forest"));
    }

    @Test
    void shouldCascadeDeleteSightingsWhenBirdIsDeleted() throws Exception {
        Bird bird = birdRepository.save(Bird.builder().name("Osprey").color("White").weight(2.0).build());
        Sighting sighting = sightingsRepository.save(Sighting.builder().bird(bird).location("Lake").date(LocalDateTime.now()).build());
        bird.addSighting(sighting);

        birdRepository.delete(bird);

        assert (sightingsRepository.count() == 0);
    }

    @Test
    void shouldOrderSightingsByLocationForSpecificBird() throws Exception {
        Bird bird = birdRepository.save(Bird.builder().name("Eagle").color("Gold").weight(4.0).build());
        sightingsRepository.save(Sighting.builder().bird(bird).location("Zion").date(LocalDateTime.now()).build());
        sightingsRepository.save(Sighting.builder().bird(bird).location("Acadia").date(LocalDateTime.now()).build());

        mockMvc.perform(get("/api/sightings/by-bird-name").param("name", "Eagle"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].location").value("Acadia"))
            .andExpect(jsonPath("$[1].location").value("Zion"));
    }
}