package com.ePatient.Models;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class BookAVisitModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int visitId;

    boolean visibility = true;
    int doctorId;
    int patientId;
    int visitHour;
    int visitMinute;
    String visitDate;
    String additionalDescription;
}
