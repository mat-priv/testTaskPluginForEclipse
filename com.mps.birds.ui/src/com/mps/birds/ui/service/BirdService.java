package com.mps.birds.ui.service;

import java.util.List;

import com.mps.birds.core.BirdDto;
import com.mps.birds.core.BirdsWsClient;

public class BirdService {

	private BirdsWsClient birdsWsClient = new BirdsWsClient();

	public void addNewBird(String name, String color, Double weight, Double height) throws Exception {
		BirdDto bird = new BirdDto.Builder().name(name).color(color).weight(weight).height(height).build();
		birdsWsClient.addBird(bird);

	}

	public List<BirdDto> getAllBirds() throws Exception {
		return birdsWsClient.getAllBirds();
	}

}
