package com.musalasoft.dronesapp.dto.in;

import com.musalasoft.dronesapp.persistence.entities.enums.EModel;
import com.musalasoft.dronesapp.persistence.entities.enums.EState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneInDTO {
    private String serialNumber;
    private EModel model;
    private Float weight;
    private Float batteryCapacity;
    private EState state;
}
