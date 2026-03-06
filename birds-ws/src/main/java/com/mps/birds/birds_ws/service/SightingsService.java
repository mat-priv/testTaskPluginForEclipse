package com.mps.birds.birds_ws.service;

import com.mps.birds.birds_ws.dto.SightingDto;
import com.mps.birds.birds_ws.exception.ResourceNotFoundException;
import com.mps.birds.birds_ws.mapper.SightingsMapper;
import com.mps.birds.birds_ws.model.Bird;
import com.mps.birds.birds_ws.model.Sighting;
import com.mps.birds.birds_ws.repository.BirdRepository;
import com.mps.birds.birds_ws.repository.SightingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SightingsService {

    private final SightingsRepository sightingsRepository;
    private final BirdRepository birdRepository;
    private final SightingsMapper sightingsMapper;

    public List<SightingDto> findAllSightings() {
        List<Sighting> result = sightingsRepository.findAllByOrderByLocation();
        log.info("Fetched list of sightings: {}", result);
        return sightingsMapper.toDtoList(result);
    }

    public List<SightingDto> findSightingsByLocation(String location) {
        List<Sighting> result = sightingsRepository.findSightingsByLocation(location);
        log.info("Found {} sightings with location: {}", result.size(), location);
        return sightingsMapper.toDtoList(result);
    }

    public List<SightingDto> findSightingsByTimeInterval(LocalDateTime start, LocalDateTime end) {
        List<Sighting> result = sightingsRepository.findSightingsByDateBetween(start, end);
        log.info("Found {} sightings for time range from {}, to {}", result.size(), start, end);
        return sightingsMapper.toDtoList(result);
    }

    @Transactional
    public SightingDto createSighting(SightingDto sightingDto) {
        Optional<Bird> bird = birdRepository.findBirdByName(sightingDto.getBirdName());
        if (bird.isEmpty()) {
            throw new ResourceNotFoundException("Cannot save the sighting, because there is no bird with name: "
                + sightingDto.getBirdName());
        }
        Sighting sighting = sightingsMapper.toEntity(sightingDto);
        sighting.setBird(bird.get());
        return sightingsMapper.toDto(sightingsRepository.save(sighting));
    }

    public List<SightingDto> findSightingsByBirdName(String name) {
        List<Sighting> result = sightingsRepository.findSightingByBird_NameOrderByLocation(name);
        log.info("Found {} sightings with bird name: {}", result.size(), name);
        return sightingsMapper.toDtoList(result);
    }
}
