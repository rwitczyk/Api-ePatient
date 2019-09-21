package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int doctorId;

    @NotBlank
    String name;

    @NotBlank
    String surname;

    @Email
    String email;

    @NotBlank
    String password;

    String profession;

    String roomNumber;

    @NotBlank
    String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(targetClass = Dates.class)
    List<Dates> days;
}
