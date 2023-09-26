package com.musalasoft.dronesapp.services;

import com.musalasoft.dronesapp.dto.in.DroneInDTO;
import com.musalasoft.dronesapp.dto.out.DroneOutDTO;
import com.musalasoft.dronesapp.exceptions.DroneLowBatteryException;
import com.musalasoft.dronesapp.exceptions.DroneNotFoundException;
import com.musalasoft.dronesapp.exceptions.DroneOverloadException;
import com.musalasoft.dronesapp.exceptions.MedicationNotFoundException;
import com.musalasoft.dronesapp.mappers.DroneInDTOToDrone;
import com.musalasoft.dronesapp.mappers.DroneToDroneOutDTO;
import com.musalasoft.dronesapp.persistence.entities.Drone;
import com.musalasoft.dronesapp.persistence.entities.Medication;
import com.musalasoft.dronesapp.persistence.entities.enums.EModel;
import com.musalasoft.dronesapp.persistence.entities.enums.EState;
import com.musalasoft.dronesapp.persistence.repositories.IDroneRepository;
import com.musalasoft.dronesapp.persistence.repositories.IMedicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {
    @Mock
    private IDroneRepository repository;
    @Mock
    private IMedicationRepository medicationRepository;
    @Mock
    private DroneInDTOToDrone mapperIn;
    @Mock
    private DroneToDroneOutDTO mapperOut;
    @InjectMocks
    private DroneServiceImpl droneService;

   @Test
    void save() {
        DroneInDTO droneInDTOException = new DroneInDTO();
        droneInDTOException.setModel(EModel.Heavyweight);
        droneInDTOException.setState(EState.LOADING);
        droneInDTOException.setWeight(50f);
        droneInDTOException.setSerialNumber("123P");
        droneInDTOException.setBatteryCapacity(23f);

        Assertions.assertThrows(DroneLowBatteryException.class, () -> {
            droneService.save(droneInDTOException);
        });

        DroneInDTO droneInDTO = new DroneInDTO();
        droneInDTO.setModel(EModel.Heavyweight);
        droneInDTO.setState(EState.DELIVERED);
        droneInDTO.setWeight(100f);
        droneInDTO.setSerialNumber("test");
        droneInDTO.setBatteryCapacity(100f);

        Mockito.when(mapperIn.map(droneInDTO)).thenReturn(new Drone(
                1L, droneInDTO.getSerialNumber(), droneInDTO.getModel(),
                droneInDTO.getWeight(),droneInDTO.getBatteryCapacity(),
                droneInDTO.getState(),null,null
        ));

        Drone expected = mapperIn.map(droneInDTO);
        Mockito.when(repository.save(mapperIn.map(droneInDTO))).thenReturn(expected);
        Drone droneSaved = repository.save(mapperIn.map(droneInDTO));
        Assertions.assertEquals(droneSaved.getId(),expected.getId());
        Assertions.assertEquals(droneSaved.getModel(),expected.getModel());
        Assertions.assertEquals(droneSaved.getBatteryCapacity(),expected.getBatteryCapacity());
        Assertions.assertEquals(droneSaved.getSerialNumber(),expected.getSerialNumber());
        Assertions.assertEquals(droneSaved.getState(),expected.getState());
        Assertions.assertEquals(droneSaved.getWeight(),expected.getWeight());
        Assertions.assertNull(droneSaved.getMedications());
        Assertions.assertNull(droneSaved.getAudits());

    }

    @Test
    void uploadMedication() {
        Drone droneA = new Drone();
        droneA.setId(1l);
        droneA.setModel(EModel.Heavyweight);
        droneA.setState(EState.IDLE);
        droneA.setWeight(100f);
        droneA.setSerialNumber("testA");
        droneA.setBatteryCapacity(50f);

        Medication medicationA = new Medication();
        medicationA.setId(1l);
        medicationA.setCode("222222");
        medicationA.setName("testMedication");
        medicationA.setWeight(120f);
        medicationA.setImage("imageNone");

        Medication medicationB = new Medication();
        medicationB.setId(1l);
        medicationB.setCode("333333");
        medicationB.setName("testMedication");
        medicationB.setWeight(40f);
        medicationB.setImage("imageNone");

        Mockito.when(repository.getDroneBySerialNumber("111111")).thenReturn(null);
        Mockito.when(repository.getDroneBySerialNumber("testA")).thenReturn(droneA);
        Mockito.when(medicationRepository.getMedicationByCode("444444")).thenReturn(null);
        Mockito.when(medicationRepository.getMedicationByCode("222222")).thenReturn(medicationA);
        Mockito.when(medicationRepository.getMedicationByCode("333333")).thenReturn(medicationB);

        Assertions.assertThrows(DroneNotFoundException.class, () -> {
            droneService.uploadMedication("111111","222222");
        });

        Assertions.assertThrows(MedicationNotFoundException.class, () -> {
            droneService.uploadMedication("testA","444444");
        });

        Assertions.assertThrows(DroneOverloadException.class, () -> {
            droneService.uploadMedication("testA","222222");
        });

        try{
            droneService.uploadMedication("testA","333333");
            Assertions.assertEquals(1,droneA.getMedications().size());
            Assertions.assertEquals(droneA.getMedications().get(0),medicationB);
        } catch (DroneNotFoundException | MedicationNotFoundException | DroneOverloadException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getMedications() {
        Assertions.assertThrows(DroneNotFoundException.class, () -> {
            droneService.uploadMedication("111111",Mockito.anyString());
        });

        DroneInDTO droneInDTO = new DroneInDTO();
        droneInDTO.setModel(EModel.Heavyweight);
        droneInDTO.setState(EState.DELIVERED);
        droneInDTO.setWeight(100f);
        droneInDTO.setSerialNumber("test");
        droneInDTO.setBatteryCapacity(100f);

        Mockito.when(mapperIn.map(droneInDTO)).thenReturn(new Drone(
                1L, droneInDTO.getSerialNumber(), droneInDTO.getModel(),
                droneInDTO.getWeight(),droneInDTO.getBatteryCapacity(),
                droneInDTO.getState(),null,null
        ));

        Drone expected = mapperIn.map(droneInDTO);
        expected.setMedications(new ArrayList<>());
        expected.getMedications().add(
                new Medication(1L,"TestMedication",
                        12f,"123456","imageNone"));
        Mockito.when(repository.save(mapperIn.map(droneInDTO))).thenReturn(expected);
        Drone droneSaved = repository.save(mapperIn.map(droneInDTO));
        Assertions.assertEquals(1,droneSaved.getMedications().size());
        Assertions.assertEquals(1,droneSaved.getMedications().get(0).getId());
        Assertions.assertEquals("TestMedication",droneSaved.getMedications().get(0).getName());
        Assertions.assertEquals(12f,droneSaved.getMedications().get(0).getWeight());
        Assertions.assertEquals("123456",droneSaved.getMedications().get(0).getCode());
        Assertions.assertEquals("imageNone",droneSaved.getMedications().get(0).getImage());

    }

    @Test
    void availableDroneForLoading() {
        Drone droneA = new Drone();
        droneA.setId(1l);
        droneA.setModel(EModel.Heavyweight);
        droneA.setState(EState.IDLE);
        droneA.setWeight(100f);
        droneA.setSerialNumber("testA");
        droneA.setBatteryCapacity(100f);

        Drone droneB = new Drone();
        droneB.setId(2l);
        droneB.setState(EState.LOADED);
        droneB.setModel(EModel.Heavyweight);
        droneB.setWeight(200f);
        droneB.setSerialNumber("testB");
        droneB.setBatteryCapacity(100f);

        Drone droneC = new Drone();
        droneC.setId(3l);
        droneC.setModel(EModel.Heavyweight);
        droneC.setState(EState.LOADING);
        droneC.setWeight(100f);
        droneC.setSerialNumber("testC");
        droneC.setBatteryCapacity(50f);



        Medication medication = new Medication(1l,"TestMedication",
                100f,"123456","imageNone");
        droneC.setMedications(new ArrayList<>());
        droneC.getMedications().add(medication);

        List<Drone> drones = new ArrayList<>();
        drones.add(droneA);
        drones.add(droneB);
        drones.add(droneC);

        DroneOutDTO droneOutDTO = DroneOutDTO
                .builder()
                .model(droneB.getModel())
                .medications(null)
                .batteryCapacity(droneB.getBatteryCapacity())
                .weight(droneB.getWeight())
                .serialNumber(droneB.getSerialNumber())
                .state(droneB.getState())
                .build();

        Mockito.when(mapperOut.map(Mockito.any(Drone.class))).thenReturn(droneOutDTO);

        Mockito.when(repository.findAll()).thenReturn(drones);

        List<DroneOutDTO> droneOutDTOS = droneService.availableDroneForLoading();
        Assertions.assertEquals(1,droneOutDTOS.size());
        Assertions.assertEquals("testB",droneOutDTOS.get(0).getSerialNumber());
    }

    @Test
    void levelBattery() {
        Drone droneA = new Drone();
        droneA.setId(1l);
        droneA.setModel(EModel.Heavyweight);
        droneA.setState(EState.IDLE);
        droneA.setWeight(100f);
        droneA.setSerialNumber("testA");
        droneA.setBatteryCapacity(100f);

        Mockito.when(repository.getDroneBySerialNumber("111111")).thenReturn(null);
        Mockito.when(repository.getDroneBySerialNumber("testA")).thenReturn(droneA);

        Assertions.assertThrows(DroneNotFoundException.class, () -> {
            droneService.levelBattery("111111");
        });

        try{
            float levelBattery = droneService.levelBattery("testA");
            Assertions.assertEquals(100f,levelBattery);
        } catch (DroneNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findDroneBySerialNumber() {
        Drone droneA = new Drone();
        droneA.setId(1l);
        droneA.setModel(EModel.Heavyweight);
        droneA.setState(EState.IDLE);
        droneA.setWeight(100f);
        droneA.setSerialNumber("testA");
        droneA.setBatteryCapacity(100f);

        Mockito.when(repository.getDroneBySerialNumber("111111")).thenReturn(null);
        Mockito.when(repository.getDroneBySerialNumber("testA")).thenReturn(droneA);

        Assertions.assertThrows(DroneNotFoundException.class, () -> {
            droneService.findDroneBySerialNumber("111111");
        });

        try{
            Drone resolve = droneService.findDroneBySerialNumber("testA");
            Assertions.assertEquals(resolve,droneA);
        } catch (DroneNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateLevelBattery() {
        Drone droneA = new Drone();
        droneA.setId(1l);
        droneA.setModel(EModel.Heavyweight);
        droneA.setState(EState.LOADING);
        droneA.setWeight(100f);
        droneA.setSerialNumber("testA");
        droneA.setBatteryCapacity(100f);

        Mockito.when(repository.getDroneBySerialNumber("111111")).thenReturn(null);
        Mockito.when(repository.getDroneBySerialNumber("testA")).thenReturn(droneA);

        Assertions.assertThrows(DroneNotFoundException.class, () -> {
            droneService.updateLevelBattery("111111",50f);
        });
        Assertions.assertThrows(DroneLowBatteryException.class, () -> {
            droneService.updateLevelBattery("testA",20f);
        });

        try{
            droneService.updateLevelBattery("testA",50f);
            Assertions.assertEquals(50f,droneA.getBatteryCapacity());
        } catch (DroneNotFoundException|DroneLowBatteryException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateState() {
        Drone droneA = new Drone();
        droneA.setId(1l);
        droneA.setModel(EModel.Heavyweight);
        droneA.setState(EState.IDLE);
        droneA.setWeight(100f);
        droneA.setSerialNumber("testA");
        droneA.setBatteryCapacity(22f);

        Mockito.when(repository.getDroneBySerialNumber("111111")).thenReturn(null);
        Mockito.when(repository.getDroneBySerialNumber("testA")).thenReturn(droneA);

        Assertions.assertThrows(DroneNotFoundException.class, () -> {
            droneService.updateState("111111",EState.LOADED);
        });
        Assertions.assertThrows(DroneLowBatteryException.class, () -> {
            droneService.updateState("testA",EState.LOADING);
        });

        try{
            droneService.updateState("testA",EState.DELIVERING);
            Assertions.assertEquals(EState.DELIVERING,droneA.getState());
        } catch (DroneNotFoundException|DroneLowBatteryException e) {
            throw new RuntimeException(e);
        }
    }

}