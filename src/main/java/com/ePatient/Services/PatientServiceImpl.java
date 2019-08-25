package com.ePatient.Services;

import com.ePatient.Entities.Patient;
import com.ePatient.Exceptions.PatientNotFoundException;
import com.ePatient.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public boolean addPatient(Patient patient) {
        patientRepository.save(patient);
        return true;
    }

    @Override
    public void deletePatient(int patientId) {
        Patient patient = getPatientById(patientId);
        if(patient != null) {
            patientRepository.delete(patient);
        }
        else {
            throw new PatientNotFoundException("Nie znaleziono pacjenta o takim id:" + patientId);
        }
    }

    @Override
    public Patient getPatientById(int patientId) {
        Patient patient =  patientRepository.getPatientByPatientId(patientId);
        if(patient != null)
        {
            return patient;
        }
        throw new PatientNotFoundException("Nie znaleziono pacjenta o takim id:" + patientId);
    }

    @Override
    public Patient getPatientByPesel(String pesel) {
        Patient patient = patientRepository.getPatientByPesel(pesel);
        if(patient != null){
            return patient;
        }
        throw new PatientNotFoundException("Nie znaleziono pacjenta o takim numerze pesel:" + pesel);
    }

    @Override
    public Iterable<Patient> getAllPatients() {
        Iterable<Patient> patients =  patientRepository.findAll();
        if(patients != null)
        {
            return patients;
        }
        throw new PatientNotFoundException("Brak pacjentow");
    }

    @Override
    public List<Patient> getPatientsByDoctorId(int doctorId) {
        List<Patient> patients = patientRepository.getPatientsByDoctorId(doctorId);
        if(patients.size() > 0){
            return patients;
        }
        throw new PatientNotFoundException("Brak pacjentow o takim id:" + doctorId + " doktora");
    }


}
