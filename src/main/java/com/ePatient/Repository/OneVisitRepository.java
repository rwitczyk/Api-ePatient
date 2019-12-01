package com.ePatient.Repository;

import com.ePatient.Entities.OneVisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OneVisitRepository extends JpaRepository<OneVisitEntity, Integer> {
    List<OneVisitEntity> getAllByPatientId(int patientId);
}
