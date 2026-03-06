package com.mps.birds.core;

import java.time.LocalDateTime;

public class SightingDto {

	private String birdName;

	private String location;

	private LocalDateTime date;

	public SightingDto() {
	}

	public SightingDto(String birdName, String location, LocalDateTime date) {
		this.location = location;
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getBirdName() {
		return birdName;
	}

	public void setBirdName(String birdName) {
		this.birdName = birdName;
	}

	private SightingDto(Builder builder) {
		this.birdName = builder.birdName;
		this.location = builder.location;
		this.date = builder.date;
	}

	public static class Builder {
		private String birdName;
		private String location;
		private LocalDateTime date;

		public Builder birdName(String birdName) {
			this.birdName = birdName;
			return this;
		}
		
		public Builder location(String location) {
			this.location = location;
			return this;
		}

		public Builder date(LocalDateTime date) {
			this.date = date;
			return this;
		}

		public SightingDto build() {
			return new SightingDto(this);
		}
	}

}
