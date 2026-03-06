package com.mps.birds.birds_ws.controller;

import com.mps.birds.birds_ws.dto.SightingDto;
import com.mps.birds.birds_ws.model.Sighting;
import com.mps.birds.birds_ws.service.SightingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sightings")
@RequiredArgsConstructor
@Tag(name = "Sightings", description = "Endpoints for managing sightings")
public class SightingsController {

    private final SightingsService sightingsService;

    @GetMapping()
    @Operation(summary = "List all sightings", description = "Returns a complete list of all recorded bird sightings")
    public List<SightingDto> getAllSightings() {
        log.info("Searching for all sightings");
        return sightingsService.findAllSightings();
    }

    @GetMapping(value = "/by-location")
    @Operation(summary = "Search by location", description = "Filters sightings based on the specific location string")
    public List<SightingDto> getSightingsByLocation(@Parameter(description = "City or landmark name", example = "Central Park")
                                                    @RequestParam String location) {
        log.info("Searching for sightings by name: {}", location);
        return sightingsService.findSightingsByLocation(location);
    }

    @GetMapping(value = "/by-bird-name")
    @Operation(summary = "Search by bird name", description = "Filters sightings based on the specific bird name")
    public List<SightingDto> getSightingsByBirdName(@Parameter(description = "Bird name", example = "Penguin")
                                                    @RequestParam String name) {
        log.info("Searching for sightings by bird name: {}", name);
        return sightingsService.findSightingsByBirdName(name);
    }

    @GetMapping(value = "/by-time", params = {"start", "end"})
    @Operation(summary = "Search by time interval", description = "Finds sightings between two timestamps")
    public List<SightingDto> getSightingsByTimeInterval(
        @Parameter(description = "Start date-time (ISO format)")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @Parameter(description = "End date-time (ISO format)")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("Searching for sightings by time range from {} to {}", start, end);
        return sightingsService.findSightingsByTimeInterval(start, end);
    }

    @PostMapping
    @Operation(
        summary = "Create a new sighting",
        responses = {
            @ApiResponse(responseCode = "201", description = "Sighting created successfully",
                content = @Content(schema = @Schema(implementation = Sighting.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided")
        }
    )
    public ResponseEntity saveSighting(@Valid @RequestBody SightingDto sightingDto) {
        log.info("Saving: {}", sightingDto);
        SightingDto sighting = sightingsService.createSighting(sightingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sighting);
    }

}
