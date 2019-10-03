package com.ePatient.Repository;

import com.ePatient.Entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity,Integer> {
    PatientEntity getPatientByPatientId(int id);
    PatientEntity getPatientByPesel(String pesel);
    List<PatientEntity> getPatientsByDoctorId(int id);
    PatientEntity getPatientByEmail(String email);
}
