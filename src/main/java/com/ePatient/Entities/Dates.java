package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Dates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dateId;

    LocalDate date;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(targetClass = Hour.class)
    List<Hour> listOfHours;
}
