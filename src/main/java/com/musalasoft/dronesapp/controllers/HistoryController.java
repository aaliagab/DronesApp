package com.musalasoft.dronesapp.controllers;

import com.musalasoft.dronesapp.dto.out.AuditOutDTO;
import com.musalasoft.dronesapp.exceptions.DroneNotFoundException;
import com.musalasoft.dronesapp.services.IAuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private final IAuditService auditService;
    private static final String MESSAGE_ERROR = "message_error";
    private static final String HISTORY = "history";

    public HistoryController(IAuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("last-ten-by-drone")
    public ResponseEntity<?> lastTenAuditsByDrone(@RequestParam String serialNumber){
        Map<String, Object> response = new HashMap<>();
        try {
            List<AuditOutDTO> auditOutDTOS = auditService.lastTenAuditsByDrone(serialNumber);
            response.put(HISTORY,auditOutDTOS);
        } catch (DroneNotFoundException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("full-history-by-drone")
    public ResponseEntity<?> auditsByDrone(@RequestParam String serialNumber){
        Map<String, Object> response = new HashMap<>();
        try {
            List<AuditOutDTO> auditOutDTOS = auditService.auditsByDrone(serialNumber);
            response.put(HISTORY,auditOutDTOS);
        } catch (DroneNotFoundException e) {
            response.put(MESSAGE_ERROR,e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
