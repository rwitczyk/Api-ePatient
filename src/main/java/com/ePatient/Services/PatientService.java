package com.ePatient.Services;

import com.ePatient.Entities.Patient;

import java.util.List;

public interface PatientService {
    boolean addPatient(Patient patient);
    void deletePatient(int id);
    Patient getPatientById(int id);
    Patient getPatientByPesel(String name);
    Iterable<Patient> getAllPatients();
    List<Patient> getPatientsByDoctorId(int id);
}
