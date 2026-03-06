package com.mps.birds.birds_ws.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SightingDto {
    @NotBlank(message = "Bird name is required")
    private String birdName;

    @NotBlank(message = "Location cannot be empty")
    @Size(max = 255, message = "Location description is too long")
    private String location;

    @NotNull(message = "Sighting date is required")
    @PastOrPresent(message = "Sighting date cannot be in the future")
    private LocalDateTime date;
}
