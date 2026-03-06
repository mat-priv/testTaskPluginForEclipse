package com.mps.birds.birds_ws.repository;

import com.mps.birds.birds_ws.model.Bird;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Resource
public interface BirdRepository extends JpaRepository<Bird, Long> {
    Optional<Bird> findBirdByName(String name);

    List<Bird> findBirdsByColor(String color);

    List<Bird> findAllByOrderByName();
}
