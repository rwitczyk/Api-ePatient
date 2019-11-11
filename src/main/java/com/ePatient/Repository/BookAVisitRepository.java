package com.ePatient.Repository;

import com.ePatient.Entities.BookAVisitModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAVisitRepository extends JpaRepository<BookAVisitModel, Integer> {
    BookAVisitModel getBookAVisitModelByVisitId(int id);
    void deleteByVisitId(int id);
}
