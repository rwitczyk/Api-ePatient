package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Hour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int hourId;

    String time;

    String isBusy;

    public Hour(String time, String isBusy) {
        this.time = time;
        this.isBusy = isBusy;
    }
}
