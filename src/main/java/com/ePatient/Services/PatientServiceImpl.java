package com.ePatient.Services;

import com.ePatient.Daos.PatientDao;
import com.ePatient.Entities.Patient;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    PatientDao patientDao;

    @Override
    public boolean addPatient(Patient patient) {
        patientDao.save(patient);
        return true;
    }

    @Override
    public boolean deletePatient(int id) {
        Patient patient = getPatientById(id);
        if(patient != null)
        {
            patientDao.delete(patient);
            return true;
        }
        return false;
    }

    @Override
    public Patient getPatientById(int id) {
        return patientDao.getPatientById(id);
    }

    @Override
    public Patient getPatientByName(String name) {
        return patientDao.getPatientByName(name);
    }

    @Override
    public Iterable<Patient> getAllPatients() {
        return patientDao.findAll();
    }
}
