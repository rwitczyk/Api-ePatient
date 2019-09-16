package com.ePatient.Endpoints;

import com.ePatient.Entities.Doctor;
import com.ePatient.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class DoctorEndpoint {

    private DoctorService doctorService;

    @Autowired
    public DoctorEndpoint(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("doctor/add")
    public ResponseEntity addDoctor(@RequestBody Doctor doctor) {
        doctorService.addDoctor(doctor);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("doctor/delete/{id}")
    public ResponseEntity deleteDoctor(@PathVariable int id) {
        doctorService.deleteDoctorById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("doctor/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
}
