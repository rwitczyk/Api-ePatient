package com.ePatient.Services;

import com.ePatient.Entities.PatientEntity;

import java.util.List;

public interface PatientService {
    void addPatient(PatientEntity patientEntity);
    void deletePatient(int id);
    PatientEntity getPatientById(int id);
    PatientEntity getPatientByPesel(String name);
    Iterable<PatientEntity> getAllPatients();
    List<PatientEntity> getPatientsByDoctorId(int id);
}
