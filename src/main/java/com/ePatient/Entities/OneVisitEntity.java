package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class OneVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int visitId;

    LocalTime fromTime;

    LocalTime toTime;

    String isBusy;

    String additionalDescription;

    public OneVisitEntity(LocalTime fromTime, LocalTime toTime, String isBusy) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.isBusy = isBusy;
    }
}
