package com.musalasoft.dronesapp.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationOutDTO {
    private String name;
    private Float weight;
    private String code;
    private String image;
}
