package com.ePatient.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiDaysDoctorTimetableModel {
    boolean mondayCheckbox;
    boolean tuesdayCheckbox;
    boolean wednesdayCheckbox;
    boolean thursdayCheckbox;
    boolean fridayCheckbox;

    TimeModel mondayVisitsFrom;
    TimeModel tuesdayVisitsFrom;
    TimeModel wednesdayVisitsFrom;
    TimeModel thursdayVisitsFrom;
    TimeModel fridayVisitsFrom;

    TimeModel mondayVisitsTo;
    TimeModel tuesdayVisitsTo;
    TimeModel wednesdayVisitsTo;
    TimeModel thursdayVisitsTo;
    TimeModel fridayVisitsTo;

    TimeModel mondayVisitsLength;
    TimeModel tuesdayVisitsLength;
    TimeModel wednesdayVisitsLength;
    TimeModel thursdayVisitsLength;
    TimeModel fridayVisitsLength;

    String rangeDates;
    int doctorId;
}
