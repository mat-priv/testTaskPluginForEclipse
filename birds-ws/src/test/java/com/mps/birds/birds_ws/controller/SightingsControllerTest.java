package com.mps.birds.birds_ws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.birds.birds_ws.dto.SightingDto;
import com.mps.birds.birds_ws.service.SightingsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SightingsController.class)
class SightingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SightingsService sightingsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/sightings should return all sightings")
    void getAllSightingsReturnsList() throws Exception {
//        GIVEN
        SightingDto dto = new SightingDto();
        dto.setLocation("Yellowstone");
        when(sightingsService.findAllSightings()).thenReturn(List.of(dto));

//        THEN
        mockMvc.perform(get("/api/sightings"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].location").value("Yellowstone"));
    }

    @Test
    @DisplayName("GET /api/sightings/by-location should filter by location parameter")
    void getSightingsByLocationReturnsFilteredList() throws Exception {
//        GIVEN
        String location = "Central Park";
        SightingDto dto = new SightingDto();
        dto.setLocation(location);
        when(sightingsService.findSightingsByLocation(location)).thenReturn(List.of(dto));

//        THEN
        mockMvc.perform(get("/api/sightings/by-location")
                .param("location", location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].location").value(location));
    }

    @Test
    @DisplayName("GET /api/sightings/by-bird-name should filter by bird name parameter")
    void getSightingsByBirdNameReturnsFilteredList() throws Exception {
//        GIVEN
        String birdName = "Penguin";
        SightingDto dto = new SightingDto();
        dto.setBirdName(birdName);
        when(sightingsService.findSightingsByBirdName(birdName)).thenReturn(List.of(dto));

//        THEN
        mockMvc.perform(get("/api/sightings/by-bird-name")
                .param("name", birdName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].birdName").value(birdName));
    }

    @Test
    @DisplayName("GET /api/sightings/by-time should handle ISO date parameters")
    void getSightingsByTimeIntervalReturnsFilteredList() throws Exception {
//        GIVEN
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 1, 12, 0);
        when(sightingsService.findSightingsByTimeInterval(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(new SightingDto()));

//        THEN
        mockMvc.perform(get("/api/sightings/by-time")
                .param("start", "2023-01-01T10:00:00")
                .param("end", "2023-01-01T12:00:00"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/sightings should return 201 Created")
    void saveSightingReturnsCreatedStatus() throws Exception {
//        GIVEN
    	LocalDateTime time = LocalDateTime.now();
    	
        SightingDto inputDto = new SightingDto();
        inputDto.setBirdName("Eagle");
        inputDto.setLocation("New York");
        inputDto.setDate(time);

        SightingDto savedDto = new SightingDto();
        savedDto.setBirdName("Eagle");
        savedDto.setLocation("New York");
        savedDto.setDate(time);

        when(sightingsService.createSighting(any(SightingDto.class))).thenReturn(savedDto);

//        THEN
        mockMvc.perform(post("/api/sightings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.birdName").value("Eagle"));
    }
}