package com.ePatient.Endpoints;

import com.ePatient.Entities.Patient;
import com.ePatient.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity addPatientAccount(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping("patient/id/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable int id)
    {
        Patient patient = patientService.getPatientById(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping("patient/delete/{id}")
    public ResponseEntity deletePatientById(@PathVariable int id)
    {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("patients/")
    public ResponseEntity<Iterable<Patient>> getAllPatients()
    {
        Iterable<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients,HttpStatus.OK);
    }

    @GetMapping("patient/pesel/{peselNumber}")
    public ResponseEntity<Patient> getPatientByPeselNumber(@PathVariable String peselNumber)
    {
        Patient patient = patientService.getPatientByPesel(peselNumber);
        return new ResponseEntity<>(patient,HttpStatus.OK);
    }

    @GetMapping("patient/doctorid/{id}")
    public ResponseEntity<List<Patient>> getPatientsByDoctorId(@PathVariable int id)
    {
        List<Patient> patients = patientService.getPatientsByDoctorId(id);
        return new ResponseEntity<>(patients,HttpStatus.OK);
    }
}
