package com.ePatient.Services;

import com.ePatient.Entities.OneVisitEntity;
import com.ePatient.Entities.PatientEntity;
import com.ePatient.Exceptions.AccountAlreadyExistsException;
import com.ePatient.Exceptions.PatientNotFoundException;
import com.ePatient.Models.PatientVisitsModel;
import com.ePatient.Repository.DoctorRepository;
import com.ePatient.Repository.OneVisitRepository;
import com.ePatient.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    private OneVisitRepository oneVisitRepository;

    private DoctorRepository doctorRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, OneVisitRepository oneVisitRepository, DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.oneVisitRepository = oneVisitRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addPatient(PatientEntity patientEntity) {
        if(!patientRepository.existsByEmail(patientEntity.getEmail())) {
            patientEntity.setPassword(passwordEncoder.encode(patientEntity.getPassword()));
            patientRepository.save(patientEntity);
        }
        else{
            throw new AccountAlreadyExistsException("Konto o takim adresie email już istnieje!");
        }
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

    @Override
    public List<PatientVisitsModel> getAllPatientVisitsByPatientId(int patientId) {
        List<OneVisitEntity> list = this.oneVisitRepository.getAllByPatientId(patientId);

        List<PatientVisitsModel> listOfPatientVisits = new ArrayList<>();
        for (OneVisitEntity oneVisit : list) {
            listOfPatientVisits.add(parseOneVisitEntityToPatientVisitsModel(oneVisit));
        }

        return listOfPatientVisits;
    }

    private PatientVisitsModel parseOneVisitEntityToPatientVisitsModel(OneVisitEntity oneVisit) {
        return PatientVisitsModel.builder()
                .visitId(oneVisit.getVisitId())
                .patientId(oneVisit.getPatientId())
                .doctor(this.doctorRepository.getDoctorByDoctorId(oneVisit.getDoctorId()))
                .fromTime(oneVisit.getFromTime())
                .toTime(oneVisit.getToTime())
                .isBusy(oneVisit.getIsBusy())
                .additionalDescription(oneVisit.getAdditionalDescription())
                .visitDate(oneVisit.getVisitDate())
                .build();
    }


}
