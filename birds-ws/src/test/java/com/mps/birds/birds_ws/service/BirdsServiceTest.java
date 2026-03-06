package com.mps.birds.birds_ws.service;

import com.mps.birds.birds_ws.dto.BirdDto;
import com.mps.birds.birds_ws.exception.ResourceNotFoundException;
import com.mps.birds.birds_ws.mapper.BirdMapper;
import com.mps.birds.birds_ws.mapper.SightingsMapper;
import com.mps.birds.birds_ws.model.Bird;
import com.mps.birds.birds_ws.repository.BirdRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirdsServiceTest1 {

    @Mock
    private BirdRepository birdRepository;

    @Mock
    private BirdMapper birdMapper;

    @Mock
    private SightingsMapper sightingsMapper;

    @InjectMocks
    private BirdsService birdsService;

    @Test
    @DisplayName("Should return a list of all birds ordered by name")
    void findAllBirdsReturnsList() {
//        GIVEN
        Bird bird = new Bird();
        List<Bird> birdList = List.of(bird);
        BirdDto birdDto = new BirdDto();

        when(birdRepository.findAllByOrderByName()).thenReturn(birdList);
        when(birdMapper.toDtoList(birdList)).thenReturn(List.of(birdDto));

//        WHEN
        List<BirdDto> result = birdsService.findAllBirds();

//        THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(birdRepository).findAllByOrderByName();
    }

    @Test
    @DisplayName("Should return a bird DTO when searching by a valid name")
    void findBirdByNameWhenExistsReturnsBirdDto() {
//        GIVEN
        String name = "Peregrine Falcon";
        Bird bird = new Bird();
        BirdDto birdDto = new BirdDto();

        when(birdRepository.findBirdByName(name)).thenReturn(Optional.of(bird));
        when(birdMapper.toDto(bird)).thenReturn(birdDto);

//        WHEN
        BirdDto result = birdsService.findBirdByName(name);

//        THEN
        assertNotNull(result);
        verify(birdRepository).findBirdByName(name);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when bird name does not exist")
    void findBirdByNameWhenNotExistsThrowsException() {
//        GIVEN
        String name = "Extinct Bird";
        when(birdRepository.findBirdByName(name)).thenReturn(Optional.empty());

//        THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            birdsService.findBirdByName(name);
        });

        verify(birdRepository).findBirdByName(name);
        verifyNoInteractions(birdMapper);
    }

    @Test
    @DisplayName("Should return birds filtered by color")
    void findBirdsByColorReturnsList() {
//        GIVEN
        String color = "Blue";
        List<Bird> birds = List.of(new Bird());
        when(birdRepository.findBirdsByColor(color)).thenReturn(birds);
        when(birdMapper.toDtoList(birds)).thenReturn(List.of(new BirdDto()));

//        WHEN
        List<BirdDto> result = birdsService.findBirdsByColor(color);

//        THEN
        assertFalse(result.isEmpty());
        verify(birdRepository).findBirdsByColor(color);
    }

    @Test
    @DisplayName("Should successfully save a new bird")
    void createBirdReturnsSavedBirdDto() {
//        GIVEN
        BirdDto inputDto = new BirdDto();
        Bird entity = new Bird();
        Bird savedEntity = new Bird();
        BirdDto outputDto = new BirdDto();

        when(birdMapper.toEntity(inputDto)).thenReturn(entity);
        when(birdRepository.save(entity)).thenReturn(savedEntity);
        when(birdMapper.toDto(savedEntity)).thenReturn(outputDto);

//        WHEN
        BirdDto result = birdsService.createBird(inputDto);

//        THEN
        assertNotNull(result);
        verify(birdRepository).save(any(Bird.class));
    }
}