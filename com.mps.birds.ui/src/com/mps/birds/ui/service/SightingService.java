package com.mps.birds.ui.service;

import java.time.LocalDateTime;
import java.util.List;

import com.mps.birds.core.SightingDto;
import com.mps.birds.core.SightingsWsClient;

public class SightingService {

	private SightingsWsClient sightingsWsClient = new SightingsWsClient();

	public void addNewSightSing(String birdName, String location, LocalDateTime date) throws Exception {
		SightingDto sighting = new SightingDto.Builder().birdName(birdName).location(location).date(date).build();

		sightingsWsClient.addSighting(sighting);
	}

	public List<SightingDto> getSightings(String birdName) throws Exception {

		if (birdName.isBlank()) {
			return sightingsWsClient.getAllSightings();
		} else {
			return sightingsWsClient.getAllSightingsByBirdName(birdName);
		}
	}

}
