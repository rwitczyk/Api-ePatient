package com.ePatient.Entities;

import com.ePatient.Models.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class DoctorEntity {

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

    Role role = Role.ROLE_DOCTOR;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(targetClass = DatesEntity.class)
    List<DatesEntity> days;
}
