package com.musalasoft.dronesapp.services;

import com.musalasoft.dronesapp.dto.in.MedicationInDTO;
import com.musalasoft.dronesapp.exceptions.MedicationNotFoundException;
import com.musalasoft.dronesapp.mappers.MedicationInDTOToMedication;
import com.musalasoft.dronesapp.persistence.entities.Medication;
import com.musalasoft.dronesapp.persistence.repositories.IMedicationRepository;
import com.musalasoft.dronesapp.services.utils.IUploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class MedicationServiceImpl implements IMedicationService{
    private final IMedicationRepository repository;
    private final MedicationInDTOToMedication mapper;
    private final IUploadFileService uploadFileService;

    public MedicationServiceImpl(IMedicationRepository repository, MedicationInDTOToMedication mapper, IUploadFileService uploadFileService) {
        this.repository = repository;
        this.mapper = mapper;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public Medication save(MedicationInDTO medicationDTO) {
        return repository.save(mapper.map(medicationDTO));
    }

    @Override
    public Optional<Medication> updateIfExist(Medication medication) {
        if(repository.existsById(medication.getId())){
            return Optional.of(repository.save(medication));
        }
        return Optional.empty();
    }

    @Override
    public String upload(MultipartFile file, Long id) throws IOException {
        return uploadFileService.copy(file,id);
    }

    @Override
    public Medication getMedicationByCode(String code) throws MedicationNotFoundException{
        Optional<Medication> medicationOptional = Optional.ofNullable(repository.getMedicationByCode(code));
        if(medicationOptional.isEmpty()){
            throw new MedicationNotFoundException("There is no medication with that code");
        }
        return medicationOptional.get();
    }
}
