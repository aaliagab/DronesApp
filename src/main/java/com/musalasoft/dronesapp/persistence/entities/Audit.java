package com.musalasoft.dronesapp.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = Drone.class)
    private Drone drone;
    @Column(columnDefinition = "FLOAT CHECK(level_battery >= 0 AND level_battery <= 100)")
    private Float levelBattery;
    private LocalTime time;
}
