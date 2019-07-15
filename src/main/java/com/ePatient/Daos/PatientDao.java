package com.ePatient.Daos;

import com.ePatient.Entities.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientDao extends CrudRepository<Patient,Integer> {
    Patient getPatientById(int id);
    Patient getPatientByName(String imie);
}
