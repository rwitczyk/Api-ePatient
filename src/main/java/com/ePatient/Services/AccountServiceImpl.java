package com.ePatient.Services;

import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Entities.PatientEntity;
import com.ePatient.Exceptions.PatientNotFoundException;
import com.ePatient.Models.LoginModel;
import com.ePatient.Models.SessionModel;
import com.ePatient.Repository.DoctorRepository;
import com.ePatient.Repository.PatientRepository;
import com.ePatient.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private JavaMailSender javaMailSender;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AccountServiceImpl(JavaMailSender javaMailSender, PatientRepository patientRepository,
                              DoctorRepository doctorRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.javaMailSender = javaMailSender;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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

    @Override
    public SessionModel signIn(LoginModel loginModel) {
        String email = loginModel.getEmail();
        String password = loginModel.getPassword();

        DoctorEntity doctorEntity = doctorRepository.getDoctorByEmail(email);
        SessionModel sessionModel = new SessionModel();

        if (doctorEntity == null) {
            PatientEntity patientEntity = patientRepository.getPatientByEmail(email);
            if (patientEntity == null) {
                throw new PatientNotFoundException("Nie ma konta o takim adresie email");
            } else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                sessionModel.setAccountId(patientEntity.getPatientId());
                sessionModel.setRole(patientEntity.getRole());
                sessionModel.setJwtToken(jwtTokenProvider.createToken(email, patientRepository.getPatientByEmail(email).getRole()));
                return sessionModel;
            }
        } else {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            sessionModel.setAccountId(doctorEntity.getDoctorId());
            sessionModel.setRole(doctorEntity.getRole());
            sessionModel.setJwtToken(jwtTokenProvider.createToken(email, doctorRepository.getDoctorByEmail(email).getRole()));
            return sessionModel;
        }
    }
}
