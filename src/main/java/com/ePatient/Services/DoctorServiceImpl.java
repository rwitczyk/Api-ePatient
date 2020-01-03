package com.ePatient.Services;

import com.ePatient.Entities.BookAVisitEntity;
import com.ePatient.Entities.DatesEntity;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Entities.OneVisitEntity;
import com.ePatient.Exceptions.AccountAlreadyExistsException;
import com.ePatient.Exceptions.CollectionIsNullException;
import com.ePatient.Exceptions.DoctorNotFoundException;
import com.ePatient.Exceptions.VisitAlreadyExistsException;
import com.ePatient.Models.BookAVisitModel;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.MultiDaysDoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;
import com.ePatient.Repository.BookAVisitRepository;
import com.ePatient.Repository.DatesRepository;
import com.ePatient.Repository.DoctorRepository;
import com.ePatient.Repository.OneVisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private static final int ONEDAYSECONDS = 86400;

    private DoctorRepository doctorRepository;

    private BookAVisitRepository bookAVisitRepository;

    private OneVisitRepository oneVisitRepository;

    private DatesRepository datesRepository;

    private PasswordEncoder passwordEncoder;


    private static Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, BookAVisitRepository bookAVisitRepository, OneVisitRepository oneVisitRepository, DatesRepository datesRepository) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookAVisitRepository = bookAVisitRepository;
        this.oneVisitRepository = oneVisitRepository;
        this.datesRepository = datesRepository;
    }

    @Override
    public void addDoctor(DoctorEntity doctorEntity) {
        if (!doctorRepository.existsByEmail(doctorEntity.getEmail())) {

            List<DatesEntity> emptyDates = new ArrayList<>();
            for (int i = 0; i < 90; i++) {
                emptyDates.add(new DatesEntity(Instant.now().plusSeconds(ONEDAYSECONDS * i)));
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
        if (list != null) {

            for (DatesEntity oneDate : list) {
                if (oneDate.getDate().toString().substring(0, 10).equals(doctorTimetableModel.getTimetableDate())) {
                    prepareAllVisitsForOneDay(
                            oneDate,
                            oneDate.getDate(),
                            LocalTime.of(doctorTimetableModel.getFromTime().getHour(), doctorTimetableModel.getFromTime().getMinute()),
                            LocalTime.of(doctorTimetableModel.getToTime().getHour(), doctorTimetableModel.getToTime().getMinute()),
                            doctorTimetableModel.getMinutes().getMinute(),
                            doctorTimetableModel.getDoctorId());
                    return;
                }
            }
            doctorEntity.setDays(list);
        } else {
            throw new CollectionIsNullException("Nie zainicjalizowana lista dni doktora!");
        }

        doctorRepository.save(doctorEntity);
    }

    private void prepareAllVisitsForOneDay(DatesEntity oneDate, Instant date, LocalTime fromTime, LocalTime toTime, int minutesInterval, int doctorId) {
        List<OneVisitEntity> listOfOneVisitEntities = new ArrayList<>();
        LocalTime actualTime = fromTime;

        while (actualTime.isBefore(toTime)) {
            listOfOneVisitEntities.add(new OneVisitEntity(doctorId, actualTime, actualTime.plusMinutes(minutesInterval), date, "false", ""));
            actualTime = actualTime.plusMinutes(minutesInterval);
        }

        oneDate.setVisitsFromTime(fromTime);
        oneDate.setVisitsToTime(toTime);
        oneDate.setDate(date);
        oneDate.setListOfOneVisitEntities(listOfOneVisitEntities);
    }

    @Override
    public void createEmptyTimetableForDoctor(DoctorTimetableModel doctorTimetableModel) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(doctorTimetableModel.getDoctorId());
        List<DatesEntity> list = doctorEntity.getDays();

        DatesEntity datesEntity = new DatesEntity();
        datesEntity.setVisitsFromTime(LocalTime.of(doctorTimetableModel.getFromTime().getHour(), doctorTimetableModel.getFromTime().getMinute()));
        datesEntity.setVisitsToTime(LocalTime.of(doctorTimetableModel.getToTime().getHour(), doctorTimetableModel.getToTime().getMinute()));
        datesEntity.setDate(Instant.parse(doctorTimetableModel.getTimetableDate()));

        list.add(datesEntity);
        doctorEntity.setDays(list);

        doctorRepository.save(doctorEntity);
    }

    @Override
    public void createEmptyOneVisit(OneVisitModel oneVisitModel) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(oneVisitModel.getDoctorId());
        OneVisitEntity oneVisitEntity = parseOneVisitModelToEntity(oneVisitModel);

        List<DatesEntity> listDoctorDates = doctorEntity.getDays();
        if (listDoctorDates != null) {
            for (DatesEntity oneDate : listDoctorDates) {
                if (oneDate.getDate().toString().substring(0, 10).equals(oneVisitModel.getVisitDate())) {
                    List<OneVisitEntity> listOfOneVisitEntities = oneDate.getListOfOneVisitEntities();
                    setVisitsFromTimeOrToTime(oneVisitEntity, oneDate);

                    listOfOneVisitEntities.add(new OneVisitEntity(oneVisitModel.getDoctorId(), oneVisitModel.getPatientId(), oneVisitEntity.getFromTime(), oneVisitEntity.getToTime(), oneDate.getDate(), "false", oneVisitModel.getAdditionalDescription()));
                    break;
                }
            }
            doctorEntity.setDays(listDoctorDates);
            doctorRepository.save(doctorEntity);
        } else {
            throw new DoctorNotFoundException("Brak listy dni doktora!");
        }
    }

    @Override
    public void createQuestionAboutBookAVisit(BookAVisitModel bookAVisitModel) {
        DoctorEntity doctor = doctorRepository.getDoctorByDoctorId(bookAVisitModel.getDoctorId());

        List<DatesEntity> listDoctorDates = doctor.getDays();
        for (DatesEntity oneDate : listDoctorDates) {
            if (oneDate.getDate().toString().substring(0, 10).equals(bookAVisitModel.getVisitDate())) {
                List<BookAVisitEntity> listOfVisitsToApprove = oneDate.getListOfVisitsToApprove();
                bookAVisitModel.setVisitDate(bookAVisitModel.getVisitDate());
                listOfVisitsToApprove.add(parseBookAVisitModelToEntity(bookAVisitModel));

                return;
            }
        }
    }

    private BookAVisitEntity parseBookAVisitModelToEntity(BookAVisitModel bookAVisitModel) {
        try {
            return BookAVisitEntity.builder()
                    .doctorId(bookAVisitModel.getDoctorId())
                    .patientId(bookAVisitModel.getPatientId())
                    .additionalDescription(bookAVisitModel.getAdditionalDescription())
                    .visibility(bookAVisitModel.isVisibility())
                    .visitDate(parseStringToInstantObject(bookAVisitModel.getVisitDate()))
                    .visitHour(bookAVisitModel.getVisitHour())
                    .visitMinute(bookAVisitModel.getVisitMinute())
                    .visitId(bookAVisitModel.getVisitId())
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BookAVisitEntity getBookAVisitModelById(int id) {
        BookAVisitEntity bookAVisitEntity = this.bookAVisitRepository.getBookAVisitModelByVisitId(id);
        if (bookAVisitEntity == null) {
            throw new DoctorNotFoundException("Nie znaleziono wizyty o id: " + id);
        }

        return bookAVisitEntity;
    }

    @Override
    public void approveBookAVisit(OneVisitModel oneVisitModel) {
        DoctorEntity doctor = doctorRepository.getDoctorByDoctorId(oneVisitModel.getDoctorId());
        OneVisitEntity oneVisitEntity = parseOneVisitModelToEntity(oneVisitModel);
        oneVisitEntity.setVisitDate(oneVisitEntity.getVisitDate());
        List<DatesEntity> listDoctorDates = doctor.getDays();
        for (DatesEntity oneDate : listDoctorDates) {
            if (oneDate.getDate().toString().substring(0, 10).equals(oneVisitEntity.getVisitDate().toString().substring(0, 10))) {
                List<OneVisitEntity> doctorVisits = oneDate.getListOfOneVisitEntities();
                oneDate.setDate(oneDate.getDate());
                doctorVisits.add(oneVisitEntity);

                setVisitsFromTimeOrToTime(oneVisitEntity, oneDate);

                BookAVisitEntity bookAVisitEntity = this.bookAVisitRepository.getBookAVisitModelByVisitId(oneVisitModel.getBookAVisitModelId());
                bookAVisitEntity.setVisibility(false);
                logger.info("Archiwizuję zapytanie o wizytę o id:" + oneVisitModel.getBookAVisitModelId() + " doktora o id:" + oneVisitModel.getDoctorId() + ", godzina: " + LocalTime.now());

                return;
            }
        }
    }

    @Override
    public void cancelVisitToAccept(int visitId) {
        BookAVisitEntity bookAVisitModel = this.bookAVisitRepository.getBookAVisitModelByVisitId(visitId);
        bookAVisitModel.setVisibility(false);
        logger.info("Archiwizuję zapytanie o wizytę o id:" + visitId + " doktora o id:" + ", godzina: " + LocalTime.now());
    }

    @Override
    public void reserveAVisit(int patientId, int visitId, String visitDescription) {
        OneVisitEntity oneVisitEntity = oneVisitRepository.getByVisitId(visitId);
        oneVisitEntity.setPatientId(patientId);
        oneVisitEntity.setIsBusy("true");
        oneVisitEntity.setAdditionalDescription(visitDescription);
        oneVisitRepository.save(oneVisitEntity);
    }

    @Override
    public void changeOneDayDescription(int dateId, String description) {
        DatesEntity datesEntity = datesRepository.getByDateId(dateId);
        datesEntity.setOneDayDescription(description);
        datesEntity.setDate(datesEntity.getDate());
        logger.info("Zmieniam opis dnia");
    }

    @Override
    public void createMultiDaysDoctorTimetable(MultiDaysDoctorTimetableModel multiDaysDoctorTimetableModel) throws ParseException {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(multiDaysDoctorTimetableModel.getDoctorId());

        List<DatesEntity> list = doctorEntity.getDays();

        String[] rangeDateToParse = multiDaysDoctorTimetableModel.getRangeDates().split(" - ");
        Instant startRangeDate, endRangeDate;
        startRangeDate = new SimpleDateFormat("MM/dd/yyyy").parse(rangeDateToParse[0]).toInstant().plusSeconds(ONEDAYSECONDS);
        endRangeDate = new SimpleDateFormat("MM/dd/yyyy").parse(rangeDateToParse[1]).toInstant().plusSeconds(ONEDAYSECONDS);

        if (list != null) {
            for (DatesEntity oneDate : list) {
                if (oneDate.getDate().isAfter(startRangeDate) && oneDate.getDate().isBefore(endRangeDate)) {
                    switch (oneDate.getDate().atZone(ZoneId.of("UTC")).getDayOfWeek()) {
                        case MONDAY: {
                            if (multiDaysDoctorTimetableModel.isMondayCheckbox()) {
                                prepareAllVisitsForOneDay(oneDate,
                                        oneDate.getDate(),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getMondayVisitsFrom().getHour(), multiDaysDoctorTimetableModel.getMondayVisitsFrom().getMinute()),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getMondayVisitsTo().getHour(), multiDaysDoctorTimetableModel.getMondayVisitsTo().getMinute()),
                                        (multiDaysDoctorTimetableModel.getMondayVisitsLength().getHour() * 60) + multiDaysDoctorTimetableModel.getMondayVisitsLength().getMinute(),
                                        multiDaysDoctorTimetableModel.getDoctorId());
                            }
                            break;
                        }
                        case TUESDAY: {
                            if (multiDaysDoctorTimetableModel.isTuesdayCheckbox()) {
                                prepareAllVisitsForOneDay(oneDate,
                                        oneDate.getDate(),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getTuesdayVisitsFrom().getHour(), multiDaysDoctorTimetableModel.getTuesdayVisitsFrom().getMinute()),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getTuesdayVisitsTo().getHour(), multiDaysDoctorTimetableModel.getTuesdayVisitsTo().getMinute()),
                                        (multiDaysDoctorTimetableModel.getTuesdayVisitsLength().getHour() * 60) + multiDaysDoctorTimetableModel.getTuesdayVisitsLength().getMinute(),
                                        multiDaysDoctorTimetableModel.getDoctorId());
                            }
                            break;
                        }
                        case WEDNESDAY: {
                            if (multiDaysDoctorTimetableModel.isWednesdayCheckbox()) {
                                prepareAllVisitsForOneDay(oneDate,
                                        oneDate.getDate(),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getWednesdayVisitsFrom().getHour(), multiDaysDoctorTimetableModel.getWednesdayVisitsFrom().getMinute()),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getWednesdayVisitsTo().getHour(), multiDaysDoctorTimetableModel.getWednesdayVisitsTo().getMinute()),
                                        (multiDaysDoctorTimetableModel.getWednesdayVisitsLength().getHour() * 60) + multiDaysDoctorTimetableModel.getWednesdayVisitsLength().getMinute(),
                                        multiDaysDoctorTimetableModel.getDoctorId());
                            }
                            break;
                        }
                        case THURSDAY: {
                            if (multiDaysDoctorTimetableModel.isThursdayCheckbox()) {
                                prepareAllVisitsForOneDay(oneDate,
                                        oneDate.getDate(),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getThursdayVisitsFrom().getHour(), multiDaysDoctorTimetableModel.getThursdayVisitsFrom().getMinute()),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getThursdayVisitsTo().getHour(), multiDaysDoctorTimetableModel.getThursdayVisitsTo().getMinute()),
                                        (multiDaysDoctorTimetableModel.getThursdayVisitsLength().getHour() * 60) + multiDaysDoctorTimetableModel.getThursdayVisitsLength().getMinute(),
                                        multiDaysDoctorTimetableModel.getDoctorId());
                            }
                            break;
                        }
                        case FRIDAY: {
                            if (multiDaysDoctorTimetableModel.isFridayCheckbox()) {
                                prepareAllVisitsForOneDay(oneDate,
                                        oneDate.getDate(),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getFridayVisitsFrom().getHour(), multiDaysDoctorTimetableModel.getFridayVisitsFrom().getMinute()),
                                        LocalTime.of(multiDaysDoctorTimetableModel.getFridayVisitsTo().getHour(), multiDaysDoctorTimetableModel.getFridayVisitsTo().getMinute()),
                                        (multiDaysDoctorTimetableModel.getFridayVisitsLength().getHour() * 60) + multiDaysDoctorTimetableModel.getFridayVisitsLength().getMinute(),
                                        multiDaysDoctorTimetableModel.getDoctorId());
                            }
                            break;
                        }
                    }
                }
            }
            doctorEntity.setDays(list);
        } else {
            throw new CollectionIsNullException("Nie zainicjalizowana lista dni doktora!");
        }

        doctorRepository.save(doctorEntity);
    }

    private void setVisitsFromTimeOrToTime(OneVisitEntity oneVisitEntity, DatesEntity oneDate) {
        if (oneDate.getVisitsFromTime() == null) {
            oneDate.setVisitsFromTime(oneVisitEntity.getFromTime());
            oneDate.setVisitsToTime(oneVisitEntity.getToTime());
        } else {
            if (oneVisitEntity.getFromTime().isAfter(oneDate.getVisitsToTime()) || oneVisitEntity.getToTime().isBefore(oneDate.getVisitsFromTime())) {
                if (oneDate.getVisitsFromTime().isAfter(oneVisitEntity.getFromTime())) {
                    oneDate.setVisitsFromTime(oneVisitEntity.getFromTime());
                }
                if (oneDate.getVisitsToTime().isBefore(oneVisitEntity.getToTime())) {
                    oneDate.setVisitsToTime(oneVisitEntity.getToTime());
                }
            } else {
                throw new VisitAlreadyExistsException("Istnieje już wizyta w podanym terminie!");
            }
        }
    }

    private OneVisitEntity parseOneVisitModelToEntity(OneVisitModel oneVisitModel) {
        try {
            return OneVisitEntity.builder()
                    .visitId(oneVisitModel.getVisitId())
                    .doctorId(oneVisitModel.getDoctorId())
                    .patientId(oneVisitModel.getPatientId())
                    .isBusy(oneVisitModel.getIsBusy())
                    .additionalDescription(oneVisitModel.getAdditionalDescription())
                    .visitDate(parseStringToInstantObject(oneVisitModel.getVisitDate()))
                    .fromTime(LocalTime.of(oneVisitModel.getFromTime().getHour(), oneVisitModel.getFromTime().getMinute()))
                    .toTime(LocalTime.of(oneVisitModel.getToTime().getHour(), oneVisitModel.getToTime().getMinute()))
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Instant parseStringToInstantObject(String date) throws ParseException {
        Instant returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(date.substring(0, 10)).toInstant();
        returnDate = returnDate.plusSeconds(ONEDAYSECONDS);
        return returnDate;
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

}
