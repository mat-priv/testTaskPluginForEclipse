package com.mps.birds.birds_ws.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BirdDto {
    @NotBlank(message = "Bird name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Color cannot be empty")
    private String color;

    @Positive(message = "Weight must be greater than zero")
    private double weight;

    @Positive(message = "Height must be greater than zero")
    private double height;
}
