package com.ePatient.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OneVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int visitId;

    int doctorId;

    LocalTime fromTime;

    LocalTime toTime;

    LocalDate visitDate;

    String isBusy;

    String additionalDescription;

    public OneVisitEntity(int doctorId, LocalTime fromTime, LocalTime toTime, LocalDate visitDate, String isBusy, String additionalDescription) {
        this.doctorId = doctorId;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.visitDate = visitDate;
        this.isBusy = isBusy;
        this.additionalDescription = additionalDescription;
    }
}
