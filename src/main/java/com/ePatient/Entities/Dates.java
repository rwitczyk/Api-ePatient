package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class Dates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dateId;
    LocalDate date;
    @ElementCollection
    @MapKeyColumn(name="hour")
    @Column(name="isBusy")
    @CollectionTable(name="listOfHours", joinColumns=@JoinColumn(name="id"))
    Map<String, Boolean> listOfHours;
}
