package com.mps.birds.birds_ws.repository;

import com.mps.birds.birds_ws.model.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Resource
public interface SightingsRepository extends JpaRepository<Sighting, Long> {

    List<Sighting> findSightingsByLocation(String location);

    List<Sighting> findSightingsByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Sighting> findAllByOrderByLocation();

    List<Sighting> findSightingByBird_NameOrderByLocation(String birdName);
}

