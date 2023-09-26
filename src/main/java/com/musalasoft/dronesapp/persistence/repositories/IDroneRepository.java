package com.musalasoft.dronesapp.persistence.repositories;

import com.musalasoft.dronesapp.persistence.entities.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDroneRepository extends JpaRepository<Drone, Long> {
    public Drone getDroneBySerialNumber(String serialNumber);
}
