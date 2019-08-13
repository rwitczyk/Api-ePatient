package com.ePatient.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private JavaMailSender javaMailSender;

    @Autowired
    public AccountServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Przypomnienie hasła!");
        msg.setText("ePacjent \n" +
                "Twoje hasło to: " + "abc");
        javaMailSender.send(msg);
    }
}
