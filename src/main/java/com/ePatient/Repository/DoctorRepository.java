package com.ePatient.Repository;

import com.ePatient.Entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Integer> {
    DoctorEntity getDoctorByDoctorId(int id);
    DoctorEntity getDoctorsByDays(LocalDate date);
    DoctorEntity getDoctorByEmail(String email);
    boolean existsByEmail(String email);
}
