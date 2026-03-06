package com.mps.birds.core;

public class BirdDto {
	private Long id;
    private String name;
    private String color;
    private Double weight;
    private Double height;

    public BirdDto() {}

    public BirdDto(String name, String color, Double weight, Double height) {
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.height = height;
    }
    
    private BirdDto(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.weight = builder.weight;
        this.height = builder.height;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    
    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private Double weight;
        private Double height;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public Builder height(Double height) {
            this.height = height;
            return this;
        }

        public BirdDto build() {
            return new BirdDto(this);
        }
    }
    
    
}
