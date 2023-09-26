package com.musalasoft.dronesapp.persistence.repositories;

import com.musalasoft.dronesapp.persistence.entities.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuditRepository extends JpaRepository<Audit, Long> {
}
