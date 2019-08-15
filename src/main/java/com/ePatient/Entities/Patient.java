package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int patientId;
    @NotBlank
    String name;
    @NotBlank
    String surname;
    @Email
    String email;
    @NotBlank
    String password;
    Date dateOfBirth;
    @NotBlank
    String pesel;
    @NotBlank
    String phoneNumber;
    int doctorId;
}
