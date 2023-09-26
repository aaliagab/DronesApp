package com.musalasoft.dronesapp.services;

import com.musalasoft.dronesapp.dto.in.DroneInDTO;
import com.musalasoft.dronesapp.dto.out.DroneOutDTO;
import com.musalasoft.dronesapp.dto.out.MedicationOutDTO;
import com.musalasoft.dronesapp.exceptions.DroneLowBatteryException;
import com.musalasoft.dronesapp.exceptions.DroneNotFoundException;
import com.musalasoft.dronesapp.exceptions.DroneOverloadException;
import com.musalasoft.dronesapp.exceptions.MedicationNotFoundException;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import com.musalasoft.dronesapp.persistence.entities.enums.EState;

import java.util.List;

public interface IDroneService {
    public Drone save(DroneInDTO droneDTO) throws DroneLowBatteryException;
    public void uploadMedication(String serialNumber, String medicationCode)
            throws DroneOverloadException, DroneNotFoundException, MedicationNotFoundException;
    public List<MedicationOutDTO> getMedications(String serialNumber) throws DroneNotFoundException;
    public List<DroneOutDTO> availableDroneForLoading();
    public float levelBattery(String serialNumber) throws DroneNotFoundException;
    public Drone findDroneBySerialNumber(String serialNumber) throws DroneNotFoundException;
    public void updateLevelBattery(String serialNumber, Float levelBattery) throws DroneNotFoundException, DroneLowBatteryException;
    public void updateState(String serialNumber, EState state) throws DroneNotFoundException, DroneLowBatteryException;
}
