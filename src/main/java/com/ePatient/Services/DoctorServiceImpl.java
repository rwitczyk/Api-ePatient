package com.ePatient.Services;

import com.ePatient.Entities.BookAVisitModel;
import com.ePatient.Entities.DatesEntity;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Entities.OneVisitEntity;
import com.ePatient.Exceptions.AccountAlreadyExistsException;
import com.ePatient.Exceptions.DoctorNotFoundException;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;
import com.ePatient.Repository.DoctorRepository;
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

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
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
        if (list != null) { //jezeli nie zainicjalizowalismy listy wczesniej
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
    public void createOneVisit(OneVisitModel oneVisitModel) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(oneVisitModel.getDoctorId());

        List<DatesEntity> listDoctorDates = doctorEntity.getDays();
        for (DatesEntity oneDate : listDoctorDates) {
            if (oneDate.getDate().equals(oneVisitModel.getVisitDate())) {
                List<OneVisitEntity> listOfOneVisitEntities = oneDate.getListOfOneVisitEntities();
                listOfOneVisitEntities.add(new OneVisitEntity(oneVisitModel.getVisitFromTime(), oneVisitModel.getVisitToTime(), "false"));
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
                listOfVisitsToApprove.add(bookAVisitModel);

                return;
            }
        }
    }
}
