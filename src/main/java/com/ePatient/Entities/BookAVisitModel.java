package com.ePatient.Entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class BookAVisitModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int visitId;

    int doctorId;
    int patientId;
    int visitHour;
    int visitMinute;
    LocalDate visitDate;
    String additionalDescription;
}
