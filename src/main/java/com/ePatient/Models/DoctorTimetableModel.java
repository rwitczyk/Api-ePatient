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

    public DoctorTimetableModel(int doctorId, int minutes, LocalDate timetableDate, LocalTime fromTime, LocalTime toTime) {
        this.doctorId = doctorId;
        this.minutes = minutes;
        this.timetableDate = timetableDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }
}
