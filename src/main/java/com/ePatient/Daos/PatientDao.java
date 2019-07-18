package com.ePatient.Daos;

import com.ePatient.Entities.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PatientDao extends CrudRepository<Patient,Integer> {
    Patient getPatientById(int id);
    Patient getPatientByPesel(String pesel);
    List<Patient> getPatientsByDoctorId(int id);
}
