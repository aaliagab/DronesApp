package com.musalasoft.dronesapp.persistence.entities;

import com.musalasoft.dronesapp.persistence.entities.enums.EModel;
import com.musalasoft.dronesapp.persistence.entities.enums.EState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, unique = true)
    private String serialNumber;
    private EModel model;
    @Column(columnDefinition = "FLOAT CHECK(weight > 0 AND weight <= 500)")
    private Float weight;
    @Column(columnDefinition = "FLOAT CHECK(battery_capacity >= 0 AND battery_capacity <= 100)")
    private Float batteryCapacity;
    private EState state;
    @OneToMany(targetEntity = Audit.class,fetch = FetchType.LAZY, mappedBy = "drone")
    private List<Audit> audits;
    @ManyToMany(targetEntity = Medication.class, fetch = FetchType.LAZY)
    @JoinTable(name = "drone_medications", joinColumns = @JoinColumn(name = "drone"), inverseJoinColumns = @JoinColumn(name = "medications"))
    private List<Medication> medications;
}
