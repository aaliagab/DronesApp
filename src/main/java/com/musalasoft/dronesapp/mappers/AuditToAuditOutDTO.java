package com.musalasoft.dronesapp.mappers;

import com.musalasoft.dronesapp.dto.out.AuditOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Audit;
import org.springframework.stereotype.Component;

@Component
public class AuditToAuditOutDTO implements IMapper<Audit, AuditOutDTO>{
    @Override
    public AuditOutDTO map(Audit in) {
        AuditOutDTO auditOutDTO = AuditOutDTO
                .builder()
                .levelBattery(in.getLevelBattery())
                .time(in.getTime())
                .build();
        return auditOutDTO;
    }
}
