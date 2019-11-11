package com.ePatient.Entities;

import com.ePatient.Models.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class PatientEntity {

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

    LocalDate dateOfBirth;

    @NotBlank
    String pesel;

    @NotBlank
    String phoneNumber;

    Role role = Role.ROLE_PATIENT;

    int doctorId;
}
