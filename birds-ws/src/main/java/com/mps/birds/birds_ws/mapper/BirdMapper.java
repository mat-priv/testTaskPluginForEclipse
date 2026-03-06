package com.mps.birds.birds_ws.mapper;

import com.mps.birds.birds_ws.dto.BirdDto;
import com.mps.birds.birds_ws.model.Bird;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BirdMapper {
//    @Mapping(source = "sightingDto", target = "sightings")
    Bird toEntity(BirdDto birdDto);

//    @Mapping(target = "sightingDto", ignore = true)
    BirdDto toDto(Bird bird);

    List<BirdDto> toDtoList(List<Bird> birdsList);
}
