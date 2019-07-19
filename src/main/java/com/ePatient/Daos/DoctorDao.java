package com.ePatient.Daos;

import com.ePatient.Entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface DoctorDao extends JpaRepository<Doctor, Integer> {
    Doctor getDoctorByDoctorId(int id);
    Doctor getDoctorsByDays(Date date);
}
