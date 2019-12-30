package com.ePatient.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int visitId;

    boolean visibility = true;
    int doctorId;
    int patientId;
    int visitHour;
    int visitMinute;
    Instant visitDate;
    String additionalDescription;
}
