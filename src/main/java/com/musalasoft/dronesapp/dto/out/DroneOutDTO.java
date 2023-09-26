package com.musalasoft.dronesapp.dto.out;

import com.musalasoft.dronesapp.persistence.entities.enums.EModel;
import com.musalasoft.dronesapp.persistence.entities.enums.EState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DroneOutDTO {
    private String serialNumber;
    private EModel model;
    private Float weight;
    private Float batteryCapacity;
    private EState state;
    private List<MedicationOutDTO> medications;
}
