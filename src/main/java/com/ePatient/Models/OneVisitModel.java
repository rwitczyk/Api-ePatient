package com.ePatient.Models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OneVisitModel {

    int bookAVisitModelId;

    int doctorId;

    int patientId;

    TimeModel fromTime;

    TimeModel toTime;

    LocalDate visitDate;

    String isBusy;

    String additionalDescription;

}
