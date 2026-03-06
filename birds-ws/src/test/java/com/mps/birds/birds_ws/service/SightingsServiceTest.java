package com.mps.birds.birds_ws.service;

import com.mps.birds.birds_ws.dto.SightingDto;
import com.mps.birds.birds_ws.exception.ResourceNotFoundException;
import com.mps.birds.birds_ws.mapper.SightingsMapper;
import com.mps.birds.birds_ws.model.Bird;
import com.mps.birds.birds_ws.model.Sighting;
import com.mps.birds.birds_ws.repository.BirdRepository;
import com.mps.birds.birds_ws.repository.SightingsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SightingsServiceTest {

    @Mock
    private SightingsRepository sightingsRepository;

    @Mock
    private BirdRepository birdRepository;

    @Mock
    private SightingsMapper sightingsMapper;

    @InjectMocks
    private SightingsService sightingsService;

    @Test
    @DisplayName("Should return all sightings ordered by location")
    void findAllSightingsReturnsList() {
//        GIVEN
        List<Sighting> sightings = List.of(new Sighting());
        when(sightingsRepository.findAllByOrderByLocation()).thenReturn(sightings);
        when(sightingsMapper.toDtoList(sightings)).thenReturn(List.of(new SightingDto()));

//        WHEN
        List<SightingDto> result = sightingsService.findAllSightings();

//        THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sightingsRepository).findAllByOrderByLocation();
    }

    @Test
    @DisplayName("Should return sightings for a specific location")
    void findSightingsByLocationReturnsList() {
//        GIVEN
        String location = "Central Park";
        List<Sighting> sightings = List.of(new Sighting());
        when(sightingsRepository.findSightingsByLocation(location)).thenReturn(sightings);
        when(sightingsMapper.toDtoList(sightings)).thenReturn(List.of(new SightingDto()));

//        WHEN
        List<SightingDto> result = sightingsService.findSightingsByLocation(location);

//        THEN
        assertFalse(result.isEmpty());
        verify(sightingsRepository).findSightingsByLocation(location);
    }

    @Test
    @DisplayName("Should return sightings within a time interval")
    void findSightingsByTimeIntervalReturnsList() {
//        GIVEN
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<Sighting> sightings = List.of(new Sighting());

        when(sightingsRepository.findSightingsByDateBetween(start, end)).thenReturn(sightings);
        when(sightingsMapper.toDtoList(sightings)).thenReturn(List.of(new SightingDto()));

//        WHEN
        List<SightingDto> result = sightingsService.findSightingsByTimeInterval(start, end);

//        THEN
        assertNotNull(result);
        verify(sightingsRepository).findSightingsByDateBetween(start, end);
    }

    @Test
    @DisplayName("Should create sighting when bird exists")
    void createSightingWhenBirdExistsReturnsSightingDto() {
//        GIVEN
        SightingDto inputDto = new SightingDto();
        inputDto.setBirdName("Robin");

        Bird bird = new Bird();
        Sighting sightingEntity = new Sighting();
        Sighting savedSighting = new Sighting();
        SightingDto outputDto = new SightingDto();

        when(birdRepository.findBirdByName("Robin")).thenReturn(Optional.of(bird));
        when(sightingsMapper.toEntity(inputDto)).thenReturn(sightingEntity);
        when(sightingsRepository.save(sightingEntity)).thenReturn(savedSighting);
        when(sightingsMapper.toDto(savedSighting)).thenReturn(outputDto);

//        WHEN
        SightingDto result = sightingsService.createSighting(inputDto);

//        THEN
        assertNotNull(result);
        verify(birdRepository).findBirdByName("Robin");
        verify(sightingsRepository).save(any(Sighting.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when creating sighting for non-existent bird")
    void createSightingWhenBirdDoesNotExistThrowsException() {
//        GIVEN
        SightingDto inputDto = new SightingDto();
        inputDto.setBirdName("Unknown Bird");

        when(birdRepository.findBirdByName("Unknown Bird")).thenReturn(Optional.empty());

//        THEN
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sightingsService.createSighting(inputDto);
        });

        assertTrue(exception.getMessage().contains("Unknown Bird"));
        verify(sightingsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return sightings filtered by bird name")
    void findSightingsByBirdNameReturnsList() {
//        GIVEN
        String birdName = "Eagle";
        List<Sighting> sightings = List.of(new Sighting());
        when(sightingsRepository.findSightingByBird_NameOrderByLocation(birdName)).thenReturn(sightings);
        when(sightingsMapper.toDtoList(sightings)).thenReturn(List.of(new SightingDto()));

//        WHEN
        List<SightingDto> result = sightingsService.findSightingsByBirdName(birdName);

//        THEN
        assertNotNull(result);
        verify(sightingsRepository).findSightingByBird_NameOrderByLocation(birdName);
    }
}