package com.ePatient.Models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OneVisitModel {

    int bookAVisitModelId;

    int doctorId;

    TimeModel fromTime;

    TimeModel toTime;

    LocalDate visitDate;

    String isBusy;

    String additionalDescription;

}
