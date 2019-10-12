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
public class DatesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dateId;

    LocalDate date;

    LocalTime visitsFromTime;

    LocalTime visitsToTime;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(targetClass = OneVisitEntity.class)
    List<OneVisitEntity> listOfOneVisitEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(targetClass = BookAVisitModel.class)
    List<BookAVisitModel> listOfVisitToApprove = new ArrayList<>();
}
