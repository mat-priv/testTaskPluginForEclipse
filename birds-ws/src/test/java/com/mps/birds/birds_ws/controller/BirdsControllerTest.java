package com.mps.birds.birds_ws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.birds.birds_ws.dto.BirdDto;
import com.mps.birds.birds_ws.service.BirdsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BirdsController.class)
class BirdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BirdsService birdsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/birds should return all birds with 200 OK")
    void getAllBirdsReturnsList() throws Exception {
//        GIVEN
        BirdDto bird = new BirdDto();
        bird.setName("Cardinal");
        when(birdsService.findAllBirds()).thenReturn(List.of(bird));

//        THEN
        mockMvc.perform(get("/api/birds"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("Cardinal"));
    }

    @Test
    @DisplayName("GET /api/birds/names/{name} should return specific bird with 200 OK")
    void getBirdByNameReturnsBird() throws Exception {
//        GIVEN
        String birdName = "Eagle";
        BirdDto bird = new BirdDto();
        bird.setName(birdName);
        when(birdsService.findBirdByName(birdName)).thenReturn(bird);

//        THEN
        mockMvc.perform(get("/api/birds/names/{name}", birdName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(birdName));
    }

    @Test
    @DisplayName("GET /api/birds/colors/{color} should return matching birds with 200 OK")
    void getBirdsByColorReturnsList() throws Exception {
//        GIVEN
        String color = "Blue";
        BirdDto bird = new BirdDto();
        bird.setColor(color);
        when(birdsService.findBirdsByColor(color)).thenReturn(List.of(bird));

//        THEN
        mockMvc.perform(get("/api/birds/colors/{color}", color))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].color").value(color));
    }

    @Test
    @DisplayName("POST /api/birds should return 201 Created and the saved bird")
    void saveBirdReturnsCreated() throws Exception {
//        GIVEN
        BirdDto inputDto = new BirdDto();
        inputDto.setName("Owl");
        inputDto.setColor("Red");
        inputDto.setHeight(10.6);
        inputDto.setWeight(0.6);

        BirdDto savedDto = new BirdDto();
        savedDto.setName("Owl");
        savedDto.setColor("Red");
        savedDto.setHeight(10.6);
        savedDto.setWeight(0.6);
        
        when(birdsService.createBird(any(BirdDto.class))).thenReturn(savedDto);

//        THEN
        mockMvc.perform(post("/api/birds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Owl"));
    }
}