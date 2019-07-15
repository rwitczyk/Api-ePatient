package com.ePatient.Services;

import com.ePatient.Entities.Patient;

public interface PatientService {
    boolean addPatient(Patient patient);
    boolean deletePatient(int id);
    Patient getPatientById(int id);
    Patient getPatientByName(String name);
    Iterable<Patient> getAllPatients();
}
