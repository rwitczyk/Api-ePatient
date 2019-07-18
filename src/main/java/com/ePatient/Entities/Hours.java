package com.ePatient.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
public class Hours {
    Map<String, Boolean> listOfHours;
//    k v
//    9 true
//    9:30 false
}
