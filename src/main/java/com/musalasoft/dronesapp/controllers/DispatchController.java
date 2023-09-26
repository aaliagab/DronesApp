package com.musalasoft.dronesapp.controllers;

import com.musalasoft.dronesapp.dto.in.DroneInDTO;
import com.musalasoft.dronesapp.dto.out.DroneOutDTO;
import com.musalasoft.dronesapp.dto.out.MedicationOutDTO;
import com.musalasoft.dronesapp.exceptions.DroneLowBatteryException;
import com.musalasoft.dronesapp.exceptions.DroneNotFoundException;
import com.musalasoft.dronesapp.exceptions.DroneOverloadException;
import com.musalasoft.dronesapp.exceptions.MedicationNotFoundException;
import com.musalasoft.dronesapp.mappers.DroneToDroneOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import com.musalasoft.dronesapp.persistence.entities.enums.EState;
import com.musalasoft.dronesapp.services.IDroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/drones")
public class DispatchController {
    private final IDroneService droneService;
    private final DroneToDroneOutDTO droneToDroneOutDTO;
    private static final String OPERATION_CORRECTLY = "The operation has been completed satisfactorily";
    private static final String MESSAGE = "message";
    private static final String DRONE = "drone";
    private static final String MESSAGE_ERROR = "message_error";
    public DispatchController(IDroneService droneService, DroneToDroneOutDTO droneToDroneOutDTO) {
        this.droneService = droneService;
        this.droneToDroneOutDTO = droneToDroneOutDTO;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DroneInDTO droneInDTO){
        Drone drone = null;
        Map<String, Object> response = new HashMap<>();
        try{
            drone = droneService.save(droneInDTO);
        } catch (DroneLowBatteryException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
        }
        response.put(MESSAGE,OPERATION_CORRECTLY);
        response.put(DRONE,droneToDroneOutDTO.map(drone));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/upload-medication")
    public ResponseEntity<?> uploadMedication(@RequestParam String serialNumber, @RequestParam String medicationCode){
        Map<String, Object> response = new HashMap<>();
        try {
            droneService.uploadMedication(serialNumber, medicationCode);
            response.put(MESSAGE,OPERATION_CORRECTLY);
            response.put(DRONE,droneToDroneOutDTO.map(droneService.findDroneBySerialNumber(serialNumber)));
        } catch (DroneNotFoundException | MedicationNotFoundException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }  catch (DroneOverloadException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/medications/serial-number")
    public ResponseEntity<?> medicationsByDrone(@RequestParam String serialNumber){
        Map<String, Object> response = new HashMap<>();
        try {
            List<MedicationOutDTO> medicationOutDTOS = droneService.getMedications(serialNumber);
            response.put("medications",medicationOutDTOS);
        } catch (DroneNotFoundException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/available-for-loading")
    public ResponseEntity<?> availableDroneForLoading(){
        Map<String, Object> response = new HashMap<>();
        List<DroneOutDTO> droneOutDTOS = droneService.availableDroneForLoading();
        response.put("medications",droneOutDTOS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/level-battery")
    public ResponseEntity<?> levelBatteryByDrone(@RequestParam String serialNumber){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("level-battery",droneService.levelBattery(serialNumber));
        } catch (DroneNotFoundException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-level-battery")
    public ResponseEntity<?> updateLevelBattery(@RequestParam String serialNumber,
                                                @RequestParam Float levelBattery){
        Map<String, Object> response = new HashMap<>();
        try{
            droneService.updateLevelBattery(serialNumber, levelBattery);
            response.put(MESSAGE,OPERATION_CORRECTLY);
            response.put(DRONE,droneToDroneOutDTO.map(droneService.findDroneBySerialNumber(serialNumber)));
        } catch (DroneLowBatteryException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
        } catch (DroneNotFoundException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-state")
    public ResponseEntity<?> updateState(@RequestParam String serialNumber,
                                                @RequestParam EState state){
        Map<String, Object> response = new HashMap<>();
        try{
            droneService.updateState(serialNumber, state);
            response.put(MESSAGE,OPERATION_CORRECTLY);
            response.put(DRONE,droneToDroneOutDTO.map(droneService.findDroneBySerialNumber(serialNumber)));
        } catch (DroneLowBatteryException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
        } catch (DroneNotFoundException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
