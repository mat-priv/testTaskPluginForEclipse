package com.mps.birds.birds_ws.mapper;

import com.mps.birds.birds_ws.dto.SightingDto;
import com.mps.birds.birds_ws.model.Sighting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SightingsMapper {

//    @Mapping(source = "name", target = "bird.name")
    Sighting toEntity(SightingDto sightingDto);

    @Mapping(source = "bird.name", target = "birdName")
    SightingDto toDto(Sighting sighting);

    List<SightingDto> toDtoList(List<Sighting> sightingList);
}
