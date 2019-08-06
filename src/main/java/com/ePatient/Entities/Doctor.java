package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int doctorId;
    String name;
    String surname;
    String email;
    String password;
    String profession;
    String roomNumber;
    String phoneNumber;
    @ElementCollection(targetClass=Dates.class)
    List<Dates> days;
}
