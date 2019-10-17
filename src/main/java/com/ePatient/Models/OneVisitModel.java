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

}
