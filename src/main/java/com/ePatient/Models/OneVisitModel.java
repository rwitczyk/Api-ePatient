package com.ePatient.Models;

import lombok.Data;

@Data
public class OneVisitModel {

    int visitId;

    int bookAVisitModelId; // aby archiwizowac prośby o wizytę

    int doctorId;

    int patientId;

    TimeModel fromTime;

    TimeModel toTime;

    String visitDate;

    String isBusy;

    String additionalDescription;

}
