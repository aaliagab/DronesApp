package com.musalasoft.dronesapp.services;

import com.musalasoft.dronesapp.dto.out.AuditOutDTO;
import com.musalasoft.dronesapp.exceptions.DroneNotFoundException;
import com.musalasoft.dronesapp.mappers.AuditToAuditOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import com.musalasoft.dronesapp.persistence.repositories.IDroneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements IAuditService{
    private final IDroneRepository droneRepository;
    private final AuditToAuditOutDTO auditToAuditOutDTO;
    private static final String NO_DRONE = "There is no drone with that serial number";

    public AuditServiceImpl(IDroneRepository droneRepository, AuditToAuditOutDTO auditToAuditOutDTO) {
        this.droneRepository = droneRepository;
        this.auditToAuditOutDTO = auditToAuditOutDTO;
    }

    @Override
    public List<AuditOutDTO> lastTenAuditsByDrone(String serialNumber) throws DroneNotFoundException {
        Optional<Drone> droneOptional = Optional.ofNullable(droneRepository.getDroneBySerialNumber(serialNumber));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }
        List<AuditOutDTO> auditOutDTOS = droneOptional.get().getAudits().stream()
                .skip(Math.max(0, droneOptional.get().getAudits().size() - 10)).limit(10).map(
                        audit -> auditToAuditOutDTO.map(audit)
                ).collect(Collectors.toList());
        return auditOutDTOS;
    }

    @Override
    public List<AuditOutDTO> auditsByDrone(String serialNumber) throws DroneNotFoundException {
        Optional<Drone> droneOptional = Optional.ofNullable(droneRepository.getDroneBySerialNumber(serialNumber));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }
        List<AuditOutDTO> auditOutDTOS = droneOptional.get().getAudits().stream().map(
                audit -> auditToAuditOutDTO.map(audit)
        ).collect(Collectors.toList());
        return auditOutDTOS;
    }
}
