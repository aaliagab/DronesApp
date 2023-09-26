package com.musalasoft.dronesapp.services;

import com.musalasoft.dronesapp.dto.in.MedicationInDTO;
import com.musalasoft.dronesapp.exceptions.MedicationNotFoundException;
import com.musalasoft.dronesapp.persistence.entities.Medication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface IMedicationService {
    public Medication save(MedicationInDTO medicationDTO);
    public Optional<Medication> updateIfExist(Medication medication);
    public String upload(MultipartFile file, Long id) throws IOException;
    public Medication getMedicationByCode(String code) throws MedicationNotFoundException;
}
