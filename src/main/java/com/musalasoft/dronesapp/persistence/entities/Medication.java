package com.musalasoft.dronesapp.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$")
    @Column
    private String name;
    private Float weight;
    @Pattern(regexp = "^[A-Z0-9_]+$")
    @Column(unique = true)
    private String code;
    private String image;
}
