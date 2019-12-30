package com.ePatient.Models;

import lombok.Data;

@Data
public class DoctorTimetableModel {
    private int doctorId;
    private TimeModel minutes;
    private String timetableDate;
    private TimeModel fromTime;
    private TimeModel toTime;
}
