package com.ePatient.Services;

import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Entities.PatientEntity;
import com.ePatient.Repository.DoctorRepository;
import com.ePatient.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private JavaMailSender javaMailSender;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    @Autowired
    public AccountServiceImpl(JavaMailSender javaMailSender, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.javaMailSender = javaMailSender;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void sendEmail(String email) {
        String passwordAccount = "";

        PatientEntity patientEntity = patientRepository.getPatientByEmail(email);
        if (patientEntity == null) {
            DoctorEntity doctorEntity = doctorRepository.getDoctorByEmail(email);
            if (doctorEntity != null) {
                passwordAccount = doctorEntity.getPassword();
            }
        } else {
            passwordAccount = patientEntity.getPassword();
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Przypomnienie hasła!");
        if (!passwordAccount.equals("")) {
            msg.setText("ePacjent \n" +
                    "Twoje hasło to: " + passwordAccount);
        } else {
            msg.setText("Nie istnieje konto o takim adresie email!");
        }
        javaMailSender.send(msg);
    }
}
