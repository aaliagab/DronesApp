package com.musalasoft.dronesapp.services;

import com.musalasoft.dronesapp.dto.out.AuditOutDTO;
import com.musalasoft.dronesapp.exceptions.DroneNotFoundException;

import java.util.List;

public interface IAuditService {
    public List<AuditOutDTO> lastTenAuditsByDrone(String serialNumber) throws DroneNotFoundException;
    public List<AuditOutDTO> auditsByDrone(String serialNumber) throws DroneNotFoundException;
}
