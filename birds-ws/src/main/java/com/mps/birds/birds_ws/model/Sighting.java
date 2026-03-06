package com.mps.birds.birds_ws.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "bird_sightings")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Sighting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bird_id", nullable = false)
    private Bird bird;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

}
