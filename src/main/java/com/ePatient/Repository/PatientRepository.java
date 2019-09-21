package com.ePatient.Repository;

import com.ePatient.Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Patient getPatientByPatientId(int id);
    Patient getPatientByPesel(String pesel);
    List<Patient> getPatientsByDoctorId(int id);
    Patient getPatientByEmail(String email);
}
