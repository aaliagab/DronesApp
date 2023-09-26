package com.musalasoft.dronesapp.mappers;

import com.musalasoft.dronesapp.dto.in.DroneInDTO;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import org.springframework.stereotype.Component;

@Component
public class DroneInDTOToDrone implements IMapper<DroneInDTO, Drone>{
    @Override
    public Drone map(DroneInDTO in) {
        Drone drone = new Drone();
        drone.setBatteryCapacity(in.getBatteryCapacity());
        drone.setModel(in.getModel());
        drone.setSerialNumber(in.getSerialNumber());
        drone.setState(in.getState());
        drone.setWeight(in.getWeight());
        return drone;
    }
}
