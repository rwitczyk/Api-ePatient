package com.ePatient.Models;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DoctorTimetableModel {
    private int doctorId;
    private int minutes;
    private LocalDate timetableDate;
    private LocalTime fromTime;
    private LocalTime toTime;
}
