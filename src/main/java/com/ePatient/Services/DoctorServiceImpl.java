package com.ePatient.Services;

import com.ePatient.Entities.BookAVisitModel;
import com.ePatient.Entities.DatesEntity;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Entities.OneVisitEntity;
import com.ePatient.Exceptions.AccountAlreadyExistsException;
import com.ePatient.Exceptions.DoctorNotFoundException;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;
import com.ePatient.Repository.BookAVisitRepository;
import com.ePatient.Repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    private PasswordEncoder passwordEncoder;

    private BookAVisitRepository bookAVisitRepository;

    private static Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, BookAVisitRepository bookAVisitRepository) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookAVisitRepository = bookAVisitRepository;
    }

    @Override
    public void addDoctor(DoctorEntity doctorEntity) {
        if (!doctorRepository.existsByEmail(doctorEntity.getEmail())) {

            List<DatesEntity> emptyDates = new ArrayList<>();
            for (int i = 0; i < 90; i++) {
                emptyDates.add(new DatesEntity(LocalDate.now().plusDays(i)));
            }
            doctorEntity.setDays(emptyDates);
            doctorEntity.setPassword(passwordEncoder.encode(doctorEntity.getPassword()));
            doctorRepository.save(doctorEntity);
        } else {
            throw new AccountAlreadyExistsException("Konto o takim adresie email już istnieje!");
        }
    }

    @Override
    public void createAutoTimetableForDoctor(DoctorTimetableModel doctorTimetableModel) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(doctorTimetableModel.getDoctorId());

        List<DatesEntity> list = doctorEntity.getDays();
        if (list != null) { // jezeli nie zainicjalizowalismy listy wczesniej
            list.add(prepareAllVisitsForOneDay(doctorTimetableModel.getTimetableDate(), doctorTimetableModel.getFromTime(),
                    doctorTimetableModel.getToTime(), doctorTimetableModel.getMinutes()));
            doctorEntity.setDays(list);
        } else {
            List<DatesEntity> createdList = new ArrayList<>();
            createdList.add(prepareAllVisitsForOneDay(doctorTimetableModel.getTimetableDate(), doctorTimetableModel.getFromTime(),
                    doctorTimetableModel.getToTime(), doctorTimetableModel.getMinutes()));
            doctorEntity.setDays(createdList);
        }

        doctorRepository.save(doctorEntity);
    }

    @Override
    public void createEmptyTimetableForDoctor(DoctorTimetableModel doctorTimetableModel) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(doctorTimetableModel.getDoctorId());
        List<DatesEntity> list = doctorEntity.getDays();

        DatesEntity datesEntity = new DatesEntity();
        datesEntity.setVisitsFromTime(doctorTimetableModel.getFromTime());
        datesEntity.setVisitsToTime(doctorTimetableModel.getToTime());
        datesEntity.setDate(doctorTimetableModel.getTimetableDate());

        list.add(datesEntity);
        doctorEntity.setDays(list);

        doctorRepository.save(doctorEntity);
    }

    @Override
    public void createOneVisit(OneVisitEntity oneVisitModel) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(oneVisitModel.getDoctorId());

        List<DatesEntity> listDoctorDates = doctorEntity.getDays();
        for (DatesEntity oneDate : listDoctorDates) {
            if (oneDate.getDate().equals(oneVisitModel.getVisitDate())) {
                List<OneVisitEntity> listOfOneVisitEntities = oneDate.getListOfOneVisitEntities();
                listOfOneVisitEntities.add(new OneVisitEntity(oneVisitModel.getFromTime(), oneVisitModel.getToTime(), "false"));
            }
        }
    }

    private DatesEntity prepareAllVisitsForOneDay(LocalDate date, LocalTime fromTime, LocalTime toTime, int minutesInterval) {
        DatesEntity datesEntity = new DatesEntity();
        List<OneVisitEntity> listOfOneVisitEntities = datesEntity.getListOfOneVisitEntities();
        LocalTime actualTime = fromTime;

        while (actualTime.isBefore(toTime)) {
            listOfOneVisitEntities.add(new OneVisitEntity(actualTime, actualTime.plusMinutes(minutesInterval), "false"));
            actualTime = actualTime.plusMinutes(minutesInterval);
        }

        datesEntity.setVisitsFromTime(fromTime);
        datesEntity.setVisitsToTime(toTime);
        datesEntity.setDate(date);
        datesEntity.setListOfOneVisitEntities(listOfOneVisitEntities);
        return datesEntity;
    }

    @Override
    public void deleteDoctorById(int doctorId) {
        DoctorEntity doctorEntity = getDoctorById(doctorId);
        if (doctorEntity != null) {
            doctorRepository.delete(doctorEntity);
        } else {
            throw new DoctorNotFoundException("Podany doktor nie istnieje!");
        }
    }

    @Override
    public DoctorEntity getDoctorById(int doctorId) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(doctorId);
        if (doctorEntity != null) {
            return doctorEntity;
        }
        throw new DoctorNotFoundException("Podany doktor nie istnieje");
    }

    @Override
    public List<DoctorEntity> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public void createQuestionAboutBookAVisit(BookAVisitModel bookAVisitModel) {
        DoctorEntity doctor = doctorRepository.getDoctorByDoctorId(bookAVisitModel.getDoctorId());

        List<DatesEntity> listDoctorDates = doctor.getDays();
        for (DatesEntity oneDate : listDoctorDates) {
            if (oneDate.getDate().equals(bookAVisitModel.getVisitDate())) {
                List<BookAVisitModel> listOfVisitsToApprove = oneDate.getListOfVisitsToApprove();
                bookAVisitModel.setVisitDate(bookAVisitModel.getVisitDate().plusDays(1)); // bo sie dodawal dzien poprzedni :(
                listOfVisitsToApprove.add(bookAVisitModel);

                return;
            }
        }
    }

    @Override
    public BookAVisitModel getBookAVisitModelById(int id) {
        BookAVisitModel bookAVisitModel = this.bookAVisitRepository.getBookAVisitModelByVisitId(id);
        if (bookAVisitModel == null) {
            throw new DoctorNotFoundException("Nie znaleziono wizyty o id: " + id);
        }

        return bookAVisitModel;
    }

    @Override
    public void approveBookAVisit(OneVisitModel oneVisitModel) {
        DoctorEntity doctor = doctorRepository.getDoctorByDoctorId(oneVisitModel.getDoctorId());
        OneVisitEntity oneVisitEntity = parseOneVisitModelToEntity(oneVisitModel);
        oneVisitEntity.setVisitDate(oneVisitEntity.getVisitDate());
        List<DatesEntity> listDoctorDates = doctor.getDays();
        for (DatesEntity oneDate : listDoctorDates) {
            if (oneDate.getDate().equals(oneVisitEntity.getVisitDate())) {
                List<OneVisitEntity> doctorVisits = oneDate.getListOfOneVisitEntities();
                oneDate.setDate(oneDate.getDate().plusDays(1)); // nie wiem czemu sie poprzedni dzien dodaje :<
                doctorVisits.add(oneVisitEntity);

                setVisitsFromTimeOrToTime(oneVisitEntity, oneDate);

                BookAVisitModel bookAVisitModel = this.bookAVisitRepository.getBookAVisitModelByVisitId(oneVisitModel.getBookAVisitModelId());
                bookAVisitModel.setVisibility(false);
                logger.info("Archiwizuję zapytanie o wizytę o id:" + oneVisitModel.getBookAVisitModelId() + " doktora o id:" + oneVisitModel.getDoctorId() + ", godzina: " + LocalTime.now());

                return;
            }
        }
    }

    private void setVisitsFromTimeOrToTime(OneVisitEntity oneVisitEntity, DatesEntity oneDate) {
        if (oneDate.getVisitsFromTime() == null) {
            oneDate.setVisitsFromTime(oneVisitEntity.getFromTime());
            oneDate.setVisitsToTime(oneVisitEntity.getToTime());
        } else {
            if (oneDate.getVisitsFromTime().isAfter(oneVisitEntity.getFromTime())) {
                oneDate.setVisitsFromTime(oneVisitEntity.getFromTime());
            }
            if (oneDate.getVisitsToTime().isBefore(oneVisitEntity.getToTime())) {
                oneDate.setVisitsToTime(oneVisitEntity.getToTime());
            }
        }
    }

    private OneVisitEntity parseOneVisitModelToEntity(OneVisitModel oneVisitModel) {

        return OneVisitEntity.builder()
                .doctorId(oneVisitModel.getDoctorId())
                .isBusy(oneVisitModel.getIsBusy())
                .additionalDescription(oneVisitModel.getAdditionalDescription())
                .visitDate(oneVisitModel.getVisitDate())
                .fromTime(LocalTime.of(oneVisitModel.getFromTime().getHour(), oneVisitModel.getFromTime().getMinute()))
                .toTime(LocalTime.of(oneVisitModel.getToTime().getHour(), oneVisitModel.getToTime().getMinute()))
                .build();
    }


}
