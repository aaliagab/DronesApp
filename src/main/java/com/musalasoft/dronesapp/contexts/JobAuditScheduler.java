package com.musalasoft.dronesapp.contexts;

import com.musalasoft.dronesapp.persistence.entities.Audit;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import com.musalasoft.dronesapp.persistence.repositories.IAuditRepository;
import com.musalasoft.dronesapp.persistence.repositories.IDroneRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class JobAuditScheduler {
    private static final String CRON_EXPRESSION = "*/30 * * * * *"; // running each 30s
    private final IDroneRepository droneRepository;
    private final IAuditRepository auditRepository;

    public JobAuditScheduler(IDroneRepository droneRepository, IAuditRepository auditRepository) {
        this.droneRepository = droneRepository;
        this.auditRepository = auditRepository;
    }

    @Scheduled(cron = CRON_EXPRESSION)
    public void runJob(){
        List<Drone> drones = droneRepository.findAll();
        drones.forEach(drone -> {
            Audit audit = new Audit();
            audit.setTime(LocalTime.now());
            audit.setLevelBattery(drone.getBatteryCapacity());
            audit.setDrone(drone);
            auditRepository.save(audit);
        });
    }
}
