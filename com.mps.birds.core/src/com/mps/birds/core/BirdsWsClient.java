package com.mps.birds.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class BirdsWsClient {

	private static final String BASE_URL = "http://localhost:8080/api/birds";

    private final HttpClient client;
    private final ObjectMapper mapper;

    public BirdsWsClient() {
        client = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public void addBird(BirdDto birdDto) throws Exception {
        String json = mapper.writeValueAsString(birdDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new Exception("Problems connecting to the service...");
		}
   
        if(response.statusCode() != 201) {
			throw new Exception(response.body());
		}
    }

    public List<BirdDto> getAllBirds() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new Exception("Problems connecting to the service...");
		}

        BirdDto[] birds = mapper.readValue(response.body(), BirdDto[].class);
        return Arrays.asList(birds);
    }
	
}
