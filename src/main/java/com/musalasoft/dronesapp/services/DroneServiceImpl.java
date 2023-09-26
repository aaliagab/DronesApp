package com.musalasoft.dronesapp.services;

import com.musalasoft.dronesapp.dto.in.DroneInDTO;
import com.musalasoft.dronesapp.dto.out.DroneOutDTO;
import com.musalasoft.dronesapp.dto.out.MedicationOutDTO;
import com.musalasoft.dronesapp.exceptions.DroneLowBatteryException;
import com.musalasoft.dronesapp.exceptions.DroneNotFoundException;
import com.musalasoft.dronesapp.exceptions.DroneOverloadException;
import com.musalasoft.dronesapp.exceptions.MedicationNotFoundException;
import com.musalasoft.dronesapp.mappers.DroneInDTOToDrone;
import com.musalasoft.dronesapp.mappers.DroneToDroneOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import com.musalasoft.dronesapp.persistence.entities.Medication;
import com.musalasoft.dronesapp.persistence.entities.enums.EState;
import com.musalasoft.dronesapp.persistence.repositories.IDroneRepository;
import com.musalasoft.dronesapp.persistence.repositories.IMedicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneServiceImpl implements IDroneService{
    private final IDroneRepository repository;
    private final IMedicationRepository medicationRepository;
    private final DroneInDTOToDrone mapperIn;
    private final DroneToDroneOutDTO mapperOut;
    private static final String NO_DRONE = "There is no drone with that serial number";

    public DroneServiceImpl(IDroneRepository repository, IMedicationRepository medicationRepository, DroneInDTOToDrone mapperIn, DroneToDroneOutDTO mapperOut) {
        this.repository = repository;
        this.medicationRepository = medicationRepository;
        this.mapperIn = mapperIn;
        this.mapperOut = mapperOut;
    }

    @Override
    public Drone save(DroneInDTO droneDTO) throws DroneLowBatteryException {
        if(droneDTO.getBatteryCapacity()<25 && droneDTO.getState().equals(EState.LOADING)){
            throw new DroneLowBatteryException("It is not possible to register a drone" +
                    " with LOADING state if its battery level is less than 25%");
        }
        return repository.save(mapperIn.map(droneDTO));
    }

    @Override
    public void uploadMedication(String serialNumber, String medicationCode)
            throws DroneOverloadException, DroneNotFoundException, MedicationNotFoundException {
        Optional<Drone> droneOptional = Optional.ofNullable(repository.getDroneBySerialNumber(serialNumber));
        Optional<Medication> medicationOptional = Optional.ofNullable(medicationRepository.getMedicationByCode(medicationCode));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }
        if(medicationOptional.isEmpty()){
            throw new MedicationNotFoundException("There is no medication with that code");
        }
        double totalWeight = droneOptional.get().getMedications()!=null?
                droneOptional.get().getMedications().stream()
                .mapToDouble(Medication::getWeight)
                .sum():0;
        if(totalWeight + medicationOptional.get().getWeight() > droneOptional.get().getWeight()){
            throw new DroneOverloadException("The weight of the medication is" +
                    " greater than the capacity available in the drone");
        }else{
            if(droneOptional.get().getMedications()==null){
                droneOptional.get().setMedications(new ArrayList<>());
            }
            droneOptional.get().getMedications().add(medicationOptional.get());
            repository.save(droneOptional.get());
        }
    }

    @Override
    public List<MedicationOutDTO> getMedications(String serialNumber) throws DroneNotFoundException{
        Optional<Drone> droneOptional = Optional.ofNullable(repository.getDroneBySerialNumber(serialNumber));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }
        return mapperOut.map(droneOptional.get()).getMedications();
    }

    @Override
    public List<DroneOutDTO> availableDroneForLoading() {
        List<Drone> drones = repository.findAll().stream()
                .filter(drone -> !drone.getState().equals(EState.IDLE) && drone.getWeight() > 0)
                .collect(Collectors.toList());
        List<Drone> dronesFilteredByMedications = new ArrayList<>();
        drones.forEach(drone -> {
            double totalWeight = drone.getMedications()!=null?drone.getMedications().stream()
                    .mapToDouble(Medication::getWeight)
                    .sum():0;
            if (drone.getWeight() - totalWeight > 0) {
                dronesFilteredByMedications.add(drone);
            }
        });
        List<DroneOutDTO> droneOutDTOS = new ArrayList<>();
        for (Drone drone:dronesFilteredByMedications) {
            DroneOutDTO droneOutDTO = mapperOut.map(drone);
            droneOutDTOS.add(droneOutDTO);
        }
        return droneOutDTOS;
    }

    @Override
    public float levelBattery(String serialNumber) throws DroneNotFoundException{
        Optional<Drone> droneOptional = Optional.ofNullable(repository.getDroneBySerialNumber(serialNumber));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }
        return droneOptional.get().getBatteryCapacity();
    }

    @Override
    public Drone findDroneBySerialNumber(String serialNumber) throws DroneNotFoundException {
        Optional<Drone> droneOptional = Optional.ofNullable(repository.getDroneBySerialNumber(serialNumber));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }
        return droneOptional.get();
    }

    @Override
    public void updateLevelBattery(String serialNumber, Float levelBattery) throws DroneNotFoundException, DroneLowBatteryException {
        Optional<Drone> droneOptional = Optional.ofNullable(repository.getDroneBySerialNumber(serialNumber));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }else{
            if(droneOptional.get().getState().equals(EState.LOADING) && levelBattery < 25){
                throw new DroneLowBatteryException("It is not possible to keep a drone" +
                        " with LOADING state if its battery level is less than 25%." +
                        " Please change the drone state or battery level");
            }else{
                droneOptional.get().setBatteryCapacity(levelBattery);
                repository.save(droneOptional.get());
            }
        }
    }

    @Override
    public void updateState(String serialNumber, EState state) throws DroneNotFoundException, DroneLowBatteryException {
        Optional<Drone> droneOptional = Optional.ofNullable(repository.getDroneBySerialNumber(serialNumber));
        if(droneOptional.isEmpty()){
            throw new DroneNotFoundException(NO_DRONE);
        }else{
            if(state.equals(EState.LOADING) && droneOptional.get().getBatteryCapacity() < 25){
                throw new DroneLowBatteryException("It is not possible to keep a drone" +
                        " with LOADING state if its battery level is less than 25%." +
                        " Please change the drone state or battery level");
            }else{
                droneOptional.get().setState(state);
                repository.save(droneOptional.get());
            }
        }
    }
}
