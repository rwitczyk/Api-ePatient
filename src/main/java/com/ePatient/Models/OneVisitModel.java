package com.ePatient.Models;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class OneVisitModel {
    private int doctorId;
    private LocalDate visitDate;
    private LocalTime visitFromTime;
    private LocalTime visitToTime;

    public OneVisitModel(int doctorId, LocalDate visitDate, LocalTime visitFromTime, LocalTime visitToTime) {
        this.doctorId = doctorId;
        this.visitDate = visitDate;
        this.visitFromTime = visitFromTime;
        this.visitToTime = visitToTime;
    }
}
