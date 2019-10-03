package com.ePatient.Services;

import com.ePatient.Entities.PatientEntity;
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
    public boolean addPatient(PatientEntity patientEntity) {
        patientRepository.save(patientEntity);
        return true;
    }

    @Override
    public void deletePatient(int patientId) {
        PatientEntity patientEntity = getPatientById(patientId);
        if(patientEntity != null) {
            patientRepository.delete(patientEntity);
        }
        else {
            throw new PatientNotFoundException("Nie znaleziono pacjenta o takim id:" + patientId);
        }
    }

    @Override
    public PatientEntity getPatientById(int patientId) {
        PatientEntity patientEntity =  patientRepository.getPatientByPatientId(patientId);
        if(patientEntity != null)
        {
            return patientEntity;
        }
        throw new PatientNotFoundException("Nie znaleziono pacjenta o takim id:" + patientId);
    }

    @Override
    public PatientEntity getPatientByPesel(String pesel) {
        PatientEntity patientEntity = patientRepository.getPatientByPesel(pesel);
        if(patientEntity != null){
            return patientEntity;
        }
        throw new PatientNotFoundException("Nie znaleziono pacjenta o takim numerze pesel:" + pesel);
    }

    @Override
    public Iterable<PatientEntity> getAllPatients() {
        Iterable<PatientEntity> patients =  patientRepository.findAll();
        if(patients != null)
        {
            return patients;
        }
        throw new PatientNotFoundException("Brak pacjentow");
    }

    @Override
    public List<PatientEntity> getPatientsByDoctorId(int doctorId) {
        List<PatientEntity> patientEntities = patientRepository.getPatientsByDoctorId(doctorId);
        if(patientEntities.size() > 0){
            return patientEntities;
        }
        throw new PatientNotFoundException("Brak pacjentow o takim id:" + doctorId + " doktora");
    }


}
