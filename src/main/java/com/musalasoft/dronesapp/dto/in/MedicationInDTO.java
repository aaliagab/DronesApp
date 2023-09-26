package com.musalasoft.dronesapp.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationInDTO {
    private String name;
    private Float weight;
    private String code;
}
