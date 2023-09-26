package com.musalasoft.dronesapp.persistence.repositories;

import com.musalasoft.dronesapp.persistence.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicationRepository extends JpaRepository<Medication, Long> {
    public Medication getMedicationByCode(String code);
}
