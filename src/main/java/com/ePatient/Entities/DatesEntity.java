package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class DatesEntity implements Comparable<DatesEntity>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dateId;

    LocalDate date;

    LocalTime visitsFromTime;

    LocalTime visitsToTime;

    String oneDayDescription = "";

    @OneToMany(cascade = CascadeType.ALL)
    List<OneVisitEntity> listOfOneVisitEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    List<BookAVisitModel> listOfVisitsToApprove = new ArrayList<>();

    public DatesEntity(LocalDate date) {
        this.date = date;
    }

    @Override
    public int compareTo(DatesEntity o) {
        return getDate().compareTo(o.getDate());
    }
}
