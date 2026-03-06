package com.mps.birds.birds_ws.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "birds")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Bird {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "height")
    private double height;

    @OneToMany(mappedBy = "bird", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Sighting> sightings = new ArrayList<>();

    public void addSighting(Sighting sighting) {
        sightings.add(sighting);
        sighting.setBird(this);
    }

}
