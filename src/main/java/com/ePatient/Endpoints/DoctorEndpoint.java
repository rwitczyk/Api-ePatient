package com.ePatient.Endpoints;

import com.ePatient.Entities.BookAVisitEntity;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Models.BookAVisitModel;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.MultiDaysDoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;
import com.ePatient.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
        doctorService.createEmptyOneVisit(oneVisitModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("doctor/create-auto-timetable-for-doctor")
    public ResponseEntity createAutoTimetableForDoctor(@RequestBody DoctorTimetableModel doctorTimetableModel) {
        doctorService.createAutoTimetableForDoctor(doctorTimetableModel);
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

    @GetMapping("visit/{id}")
    public ResponseEntity<BookAVisitEntity> getBookAVisitModelByVisitId(@PathVariable int id) {
        return new ResponseEntity<>(doctorService.getBookAVisitModelById(id), HttpStatus.OK);
    }

    @PostMapping("visit/approve")
    public ResponseEntity approveBookAVisit(@RequestBody OneVisitModel oneVisitModel) {
        doctorService.approveBookAVisit(oneVisitModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("doctor/question-about-book-a-visit")
    public ResponseEntity questionAboutBookAVisit(@RequestBody BookAVisitModel bookAVisitModel) {
        doctorService.createQuestionAboutBookAVisit(bookAVisitModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("doctor/cancelOneVisitToAccept/{visitId}")
    public ResponseEntity cancelVisitToAccept(@PathVariable int visitId) {
        doctorService.cancelVisitToAccept(visitId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("doctor/reserveAVisit/{patientId}/{visitId}/{visitDescription}")
    public ResponseEntity reserveAVisit(@PathVariable int patientId, @PathVariable int visitId, @PathVariable String visitDescription) {
        doctorService.reserveAVisit(patientId, visitId, visitDescription);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("doctor/changeOneDayDescription/{dateId}/{description}")
    public ResponseEntity changeOneDayDescription(@PathVariable int dateId, @PathVariable String description){
        doctorService.changeOneDayDescription(dateId, description);
        return ResponseEntity.ok("");
    }

    @PostMapping("doctor/createMultiDaysTimeTable")
    public ResponseEntity createMultiDaysDoctorTimetable(@RequestBody MultiDaysDoctorTimetableModel multiDaysDoctorTimetableModel) throws ParseException {
        doctorService.createMultiDaysDoctorTimetable(multiDaysDoctorTimetableModel);
        return ResponseEntity.ok("");
    }
}
