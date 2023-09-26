package com.musalasoft.dronesapp.mappers;

import com.musalasoft.dronesapp.dto.out.DroneOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class DroneToDroneOutDTO implements IMapper<Drone, DroneOutDTO>{
    private final MedicationToMedicationOutDTO medicationMapper;

    public DroneToDroneOutDTO(MedicationToMedicationOutDTO medicationMapper) {
        this.medicationMapper = medicationMapper;
    }

    @Override
    public DroneOutDTO map(Drone in) {
        DroneOutDTO droneOutDTO = new DroneOutDTO();
        droneOutDTO.setBatteryCapacity(in.getBatteryCapacity());
        droneOutDTO.setModel(in.getModel());
        droneOutDTO.setSerialNumber(in.getSerialNumber());
        droneOutDTO.setState(in.getState());
        droneOutDTO.setWeight(in.getWeight());
        droneOutDTO.setMedications(in.getMedications()!=null?
                        in.getMedications().stream()
                                .map(medication -> medicationMapper.map(medication))
                                .collect(Collectors.toList()):new ArrayList<>());
        return droneOutDTO;
    }
}
