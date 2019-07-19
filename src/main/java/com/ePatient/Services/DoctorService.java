package com.ePatient.Services;

import com.ePatient.Entities.Doctor;

import java.util.List;

public interface DoctorService {
    void addDoctor(Doctor doctor);
    void deleteDoctorById(int id);
    Doctor getDoctorById(int id);
    List<Doctor> getAllDoctors();
}
