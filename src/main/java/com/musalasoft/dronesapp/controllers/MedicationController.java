package com.musalasoft.dronesapp.controllers;

import com.musalasoft.dronesapp.dto.in.MedicationInDTO;
import com.musalasoft.dronesapp.exceptions.MedicationNotFoundException;
import com.musalasoft.dronesapp.mappers.MedicationToMedicationOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Medication;
import com.musalasoft.dronesapp.services.IMedicationService;
import com.musalasoft.dronesapp.services.utils.IUploadFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/medications")
public class MedicationController {
    private final IMedicationService medicationService;
    private final IUploadFileService uploadFileService;
    private final MedicationToMedicationOutDTO medicationToMedicationOutDTO;

    public MedicationController(IMedicationService medicationService, IUploadFileService uploadFileService, MedicationToMedicationOutDTO medicationToMedicationOutDTO) {
        this.medicationService = medicationService;
        this.uploadFileService = uploadFileService;
        this.medicationToMedicationOutDTO = medicationToMedicationOutDTO;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MedicationInDTO medicationInDTO){
        Medication medication = null;
        Map<String, Object> response = new HashMap<>();
        try{
            medication = this.medicationService.save(medicationInDTO);
        }catch (Exception e){
            response.put("message_error",e.getMessage());
            response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message","The medication has been creating correctly");
        response.put("medication",medicationToMedicationOutDTO.map(medication));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam("code") String code){
        Map<String, Object> response = new HashMap<>();
        Medication medication = null;
        try{
            medication = medicationService.getMedicationByCode(code);
        } catch (MedicationNotFoundException e) {
            response.put("message_error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(!file.isEmpty()) {
            String fileName = null;

            try {
                fileName = uploadFileService.copy(file, medication.getId());

            } catch (IOException e) {
                response.put("message_error",e.getMessage());
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String fileBefore = medication.getImage();
            uploadFileService.delete(fileBefore);

            medication.setImage(fileName);
            if(medicationService.updateIfExist(medication).isPresent()) {
                response.put("medication", medication);
                response.put("message", "The image " + fileName + " has been uploaded correctly");
            }else{
                response.put("message","The medication is not valid");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
