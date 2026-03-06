package com.mps.birds.birds_ws.service;

import com.mps.birds.birds_ws.dto.BirdDto;
import com.mps.birds.birds_ws.exception.ResourceNotFoundException;
import com.mps.birds.birds_ws.mapper.BirdMapper;
import com.mps.birds.birds_ws.mapper.SightingsMapper;
import com.mps.birds.birds_ws.model.Bird;
import com.mps.birds.birds_ws.repository.BirdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BirdsService {

    private final BirdRepository birdRepository;
    private final BirdMapper birdMapper;
    private final SightingsMapper sightingsMapper;

    public List<BirdDto> findAllBirds() {
        List<Bird> birdList = birdRepository.findAllByOrderByName();
        log.info("Fetched list of birds: {}", birdList);
        return birdMapper.toDtoList(birdList);
    }

    public BirdDto findBirdByName(String name) {
        Optional<Bird> result = birdRepository.findBirdByName(name);
        BirdDto birdDto = result.map(bird -> {
                log.info("Found bird with name: {}", name);
                return birdMapper.toDto(bird);
            })
            .orElseThrow(() -> new ResourceNotFoundException("Bird not found with name: " + name));
        return birdDto;
    }

    public List<BirdDto> findBirdsByColor(String color) {
        List<Bird> birdList = birdRepository.findBirdsByColor(color);
        log.info("Found {} birds with color: {}", birdList.size(), color);
        return birdMapper.toDtoList(birdList);
    }

    @Transactional
    public BirdDto createBird(BirdDto birdDto) {
        Bird birdToSave = birdMapper.toEntity(birdDto);
        return birdMapper.toDto(birdRepository.save(birdToSave));
    }
}
