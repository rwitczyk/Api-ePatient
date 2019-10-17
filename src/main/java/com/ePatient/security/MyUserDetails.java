package com.ePatient.security;

import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Entities.PatientEntity;
import com.ePatient.Exceptions.PatientNotFoundException;
import com.ePatient.Repository.DoctorRepository;
import com.ePatient.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

    private DoctorRepository doctorRepository;

    private PatientRepository patientRepository;

    @Autowired
    public MyUserDetails(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        final DoctorEntity doctor = doctorRepository.getDoctorByEmail(email);

        if (doctor == null) {
            return returnPatientAccount(email);
        } else {
            return org.springframework.security.core.userdetails.User//
                    .withUsername(email)//
                    .password(doctor.getPassword())//
                    .authorities(doctor.getRole())//
                    .accountExpired(false)//
                    .accountLocked(false)//
                    .credentialsExpired(false)//
                    .disabled(false)//
                    .build();
        }
    }

    private UserDetails returnPatientAccount(String email) {
        PatientEntity patient = patientRepository.getPatientByEmail(email);

        if (patient == null) {
            throw new PatientNotFoundException("Nie znaleziono takiego konta");
        } else {
            return org.springframework.security.core.userdetails.User//
                    .withUsername(email)//
                    .password(patient.getPassword())//
                    .authorities(patient.getRole())//
                    .accountExpired(false)//
                    .accountLocked(false)//
                    .credentialsExpired(false)//
                    .disabled(false)//
                    .build();
        }
    }
}
