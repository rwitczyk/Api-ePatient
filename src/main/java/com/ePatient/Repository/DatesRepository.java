package com.ePatient.Repository;

import com.ePatient.Entities.DatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatesRepository extends JpaRepository<DatesEntity, Integer> {
    DatesEntity getByDateId(int id);
}
