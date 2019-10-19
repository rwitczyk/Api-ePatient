package com.ePatient.Endpoints;

import com.ePatient.Entities.BookAVisitModel;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;
import com.ePatient.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("doctor/add")
    public ResponseEntity addDoctor(@RequestBody DoctorEntity doctorEntity) {
        doctorService.addDoctor(doctorEntity);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("doctor/create-one-visit")
    public ResponseEntity createOneVisit(@RequestBody OneVisitModel oneVisitModel) {
        doctorService.createOneVisit(oneVisitModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("doctor/create-auto-timetable-for-doctor")
    public ResponseEntity createAutoTimetableForDoctor(@RequestBody DoctorTimetableModel doctorTimetableModel) {
        doctorService.createAutoTimetableForDoctor(doctorTimetableModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("doctor/question-about-book-a-visit")
    public ResponseEntity questionAboutBookAVisit(@RequestBody BookAVisitModel bookAVisitModel) {
        doctorService.questionAboutBookAVisit(bookAVisitModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("doctor/create-empty-timetable-for-doctor")
    public ResponseEntity createEmptyTimetableForDoctor(@RequestBody DoctorTimetableModel doctorTimetableModel) {
        doctorService.createEmptyTimetableForDoctor(doctorTimetableModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("doctor/delete/{id}")
    public ResponseEntity deleteDoctor(@PathVariable int id) {
        doctorService.deleteDoctorById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("doctor/{id}")
    public ResponseEntity<DoctorEntity> getDoctorById(@PathVariable int id) {
        DoctorEntity doctorEntity = doctorService.getDoctorById(id);
        return new ResponseEntity<>(doctorEntity, HttpStatus.OK);
    }

    @GetMapping("doctors")
    public ResponseEntity<List<DoctorEntity>> getAllDoctors() {
        List<DoctorEntity> doctorEntities = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctorEntities, HttpStatus.OK);
    }
}
