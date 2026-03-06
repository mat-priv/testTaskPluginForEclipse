package com.mps.birds.core;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SightingsWsClient {

	private static final String BASE_URL = "http://localhost:8080/api/sightings";

	private final HttpClient client;
	private final ObjectMapper mapper;

	public SightingsWsClient() {
		client = HttpClient.newHttpClient();
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	}

	public void addSighting(SightingDto sightingDto) throws Exception {
		String json = mapper.writeValueAsString(sightingDto);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL))
				.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

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

	public List<SightingDto> getAllSightings() throws Exception {

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();

		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new Exception("Problems connecting to the service...");
		}

		SightingDto[] sightings = mapper.readValue(response.body(), SightingDto[].class);
		return Arrays.asList(sightings);
	}

	public List<SightingDto> getAllSightingsByBirdName(String name) throws Exception {
		String urlWithParam = BASE_URL + "/by-bird-name?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(urlWithParam))
				.GET()
				.build();

		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new Exception("Problems connecting to the service...");
		}

		SightingDto[] sightings = mapper.readValue(response.body(), SightingDto[].class);
		return Arrays.asList(sightings);
	}

}
