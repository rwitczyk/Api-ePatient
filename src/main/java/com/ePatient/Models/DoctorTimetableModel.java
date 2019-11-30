package com.ePatient.Models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorTimetableModel {
    private int doctorId;
    private TimeModel minutes;
    private LocalDate timetableDate;
    private TimeModel fromTime;
    private TimeModel toTime;
}
