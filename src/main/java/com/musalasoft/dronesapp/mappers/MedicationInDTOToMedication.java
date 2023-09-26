package com.musalasoft.dronesapp.mappers;

import com.musalasoft.dronesapp.dto.in.MedicationInDTO;
import com.musalasoft.dronesapp.persistence.entities.Medication;
import com.musalasoft.dronesapp.services.utils.IUploadFileService;
import org.springframework.stereotype.Component;

@Component
public class MedicationInDTOToMedication implements IMapper<MedicationInDTO, Medication>{
    private final IUploadFileService uploadFileService;

    public MedicationInDTOToMedication(IUploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    @Override
    public Medication map(MedicationInDTO in) {
        Medication medication = new Medication();
        medication.setCode(in.getCode());
        medication.setName(in.getName());
        medication.setWeight(in.getWeight());
        medication.setImage(uploadFileService.path("imageNone").toAbsolutePath().toString());
        return medication;
    }
}
