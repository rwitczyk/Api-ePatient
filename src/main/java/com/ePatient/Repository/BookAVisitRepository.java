package com.ePatient.Repository;

import com.ePatient.Entities.BookAVisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAVisitRepository extends JpaRepository<BookAVisitEntity, Integer> {
    BookAVisitEntity getBookAVisitModelByVisitId(int id);
    void deleteByVisitId(int id);
}
