package com.ePatient.Services;

import com.ePatient.Entities.BookAVisitEntity;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Models.BookAVisitModel;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.MultiDaysDoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;

import java.text.ParseException;
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
    BookAVisitEntity getBookAVisitModelById(int id);
    void approveBookAVisit(OneVisitModel oneVisitModel);
    void cancelVisitToAccept(int visitId);
    void reserveAVisit(int patientId, int visitId, String visitDescription);
    void changeOneDayDescription(int dateId, String description);
    void changeOneVisitDoctorDescription(int visitId, String doctorDescription);
    String getOneVisitDoctorDescription(int visitId);
    void createMultiDaysDoctorTimetable(MultiDaysDoctorTimetableModel multiDaysDoctorTimetableModel) throws ParseException;
}
