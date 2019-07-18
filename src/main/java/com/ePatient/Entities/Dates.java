package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Dates {
    Date date;
    List<Hours> listOfHours;
}
