package com.mps.birds.birds_ws.controller;

import com.mps.birds.birds_ws.dto.BirdDto;
import com.mps.birds.birds_ws.model.Bird;
import com.mps.birds.birds_ws.service.BirdsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/birds")
@RequiredArgsConstructor
@Tag(name = "Birds", description = "Endpoints for managing birds")
public class BirdsController {
    private final BirdsService birdsService;

    @GetMapping
    @Operation(summary = "Get all birds", description = "Retrieves a list of all bird species in the catalog")
    public List<BirdDto> getAllBirds() {
        log.info("Searching for all birds");
        return birdsService.findAllBirds();
    }

    @GetMapping("/names/{name}")
    @Operation(summary = "Find bird by name", description = "Retrieves detailed information for a specific bird species by its name")
    @ApiResponse(responseCode = "200", description = "Bird found")
    @ApiResponse(responseCode = "404", description = "Bird not found")
    public BirdDto getBirdByName(@PathVariable String name) {
        log.info("Searching for bird by name: {}", name);
        return birdsService.findBirdByName(name);
    }

    @GetMapping("/colors/{color}")
    @Operation(summary = "Find birds by color", description = "Returns a list of all birds that match the specified primary color")
    public List<BirdDto> getBirdsByColor(@PathVariable String color) {
        return birdsService.findBirdsByColor(color);
    }

    @PostMapping
    @Operation(
        summary = "Register a new bird",
        description = "Adds a new bird species to the system catalog",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Bird successfully created",
                content = @Content(schema = @Schema(implementation = Bird.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid bird data provided")
        }
    )
    public ResponseEntity saveBird(@Valid @RequestBody BirdDto birdDto) {
        log.info("Saving: {}", birdDto);
        BirdDto bird = birdsService.createBird(birdDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bird);
    }
}
