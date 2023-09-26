package com.musalasoft.dronesapp.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditOutDTO {
    private Float levelBattery;
    private LocalTime time;
}
