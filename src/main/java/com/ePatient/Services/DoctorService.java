package com.ePatient.Services;

import com.ePatient.Entities.BookAVisitModel;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;

import java.util.List;

public interface DoctorService {
    void addDoctor(DoctorEntity doctorEntity);
    void createAutoTimetableForDoctor(DoctorTimetableModel doctorTimetableModel);
    void createEmptyTimetableForDoctor(DoctorTimetableModel doctorTimetableModel);
    void createEmptyOneVisit(OneVisitModel oneVisitModel);
    void deleteDoctorById(int id);
    DoctorEntity getDoctorById(int id);
    List<DoctorEntity> getAllDoctors();
    void createQuestionAboutBookAVisit(BookAVisitModel bookAVisitModel);
    BookAVisitModel getBookAVisitModelById(int id);
    void approveBookAVisit(OneVisitModel oneVisitModel);
}
