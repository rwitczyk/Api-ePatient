package com.ePatient.Endpoints;

import com.ePatient.Entities.Patient;
import com.ePatient.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PatientEndpoint {

    private PatientService patientService;

    @Autowired
    public PatientEndpoint(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("patient/add")
    public ResponseEntity<String> addPatientAccount(@RequestBody Patient patient) {
        boolean ifPatientAdded = patientService.addPatient(patient);

        if (ifPatientAdded)
        {
            return new ResponseEntity<>("Pomyslnie dodano konto", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("patient/delete/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable int id)
    {
        boolean ifPatientDeleted = patientService.deletePatient(id);

        if (ifPatientDeleted)
        {
            return new ResponseEntity<>("Pomyslnie dodano konto", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
