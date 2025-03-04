package com.ePatient.Models;

import com.ePatient.Entities.DoctorEntity;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalTime;

@Data
@Builder
public class PatientVisitsModel {
    int visitId;

    DoctorEntity doctor;

    int patientId;

    LocalTime fromTime;

    LocalTime toTime;

    Instant visitDate;

    String isBusy;

    String additionalDescription;

    String additionalDoctorDescription;
}
