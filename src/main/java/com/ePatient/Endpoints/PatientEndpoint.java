package com.ePatient.Endpoints;

import com.ePatient.Entities.PatientEntity;
import com.ePatient.Models.PatientVisitsModel;
import com.ePatient.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity addPatientAccount(@RequestBody PatientEntity patientEntity) {
        patientService.addPatient(patientEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("patient/id/{id}")
    public ResponseEntity<PatientEntity> getPatientById(@PathVariable int id) {
        PatientEntity patientEntity = patientService.getPatientById(id);
        return new ResponseEntity<>(patientEntity, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PATIENT')")
    @GetMapping("patient/delete/{id}")
    public ResponseEntity deletePatientById(@PathVariable int id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("patients")
    public ResponseEntity<Iterable<PatientEntity>> getAllPatients() {
        Iterable<PatientEntity> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("patient/pesel/{peselNumber}")
    public ResponseEntity<PatientEntity> getPatientByPeselNumber(@PathVariable String peselNumber) {
        PatientEntity patientEntity = patientService.getPatientByPesel(peselNumber);
        return new ResponseEntity<>(patientEntity, HttpStatus.OK);
    }

    @GetMapping("patient/doctorid/{id}")
    public ResponseEntity<List<PatientEntity>> getPatientsByDoctorId(@PathVariable int id) {
        List<PatientEntity> patientEntities = patientService.getPatientsByDoctorId(id);
        return new ResponseEntity<>(patientEntities, HttpStatus.OK);
    }

    @GetMapping("patient/getAllPatientVisits/{patientId}")
    public ResponseEntity<List<PatientVisitsModel>> getAllPatientVisnntitsByPatientId(@PathVariable int patientId) {
        List<PatientVisitsModel> patientVisits = patientService.getAllPatientVisitsByPatientId(patientId);
        return new ResponseEntity<>(patientVisits, HttpStatus.OK);
    }
}
