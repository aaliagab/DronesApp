package com.musalasoft.dronesapp.mappers;

import com.musalasoft.dronesapp.dto.out.MedicationOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Medication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MedicationToMedicationOutDTO implements IMapper<Medication, MedicationOutDTO>{
    private final ResourceLoader resourceLoader;

    public MedicationToMedicationOutDTO(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public MedicationOutDTO map(Medication in) {
        Resource resource = resourceLoader.getResource("classpath:/static/" +
                new File(in.getImage()).toPath().getFileName().toString());
        MedicationOutDTO medicationOutDTO = null;
        try {
            medicationOutDTO = MedicationOutDTO
                    .builder()
                    .code(in.getCode())
                    .name(in.getName())
                    .image(resource.getURL().toString())
                    .weight(in.getWeight())
                    .build();
        }catch (IOException io){
            medicationOutDTO = MedicationOutDTO
                    .builder()
                    .code(in.getCode())
                    .name(in.getName())
                    .image("imageNone")
                    .weight(in.getWeight())
                    .build();
        }
        return medicationOutDTO;
    }
}
