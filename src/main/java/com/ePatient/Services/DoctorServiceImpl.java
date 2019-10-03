package com.ePatient.Services;

import com.ePatient.Entities.DatesEntity;
import com.ePatient.Entities.DoctorEntity;
import com.ePatient.Entities.OneVisitEntity;
import com.ePatient.Exceptions.DoctorNotFoundException;
import com.ePatient.Models.DoctorTimetableModel;
import com.ePatient.Models.OneVisitModel;
import com.ePatient.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void addDoctor(DoctorEntity doctorEntity) {
        List<DatesEntity> list = new ArrayList<>();
        doctorEntity.setDays(list);
        doctorRepository.save(doctorEntity);
    }

    @Override
    public void createAutoTimetableForDoctor(DoctorTimetableModel doctorTimetableModel) {
        DoctorEntity doctorEntity = doctorRepository.getDoctorByDoctorId(doctorTimetableModel.getDoctorId());

        List<DatesEntity> list = doctorEntity.getDays();
        list.add(prepareOneDay(doctorTimetableModel.getTimetableDate(), doctorTimetableModel.getFromTime(),
                doctorTimetableModel.getToTime(), doctorTimetableModel.getMinutes()));
        doctorEntity.setDays(list);

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

    private DatesEntity prepareOneDay(LocalDate date, LocalTime fromTime, LocalTime toTime, int minutesInterval) {
        List<OneVisitEntity> listOfOneVisitEntities = new ArrayList<>();
        LocalTime actualTime = fromTime;

        while (actualTime.isBefore(toTime)) {
            listOfOneVisitEntities.add(new OneVisitEntity(actualTime, actualTime.plusMinutes(minutesInterval), "false"));
            actualTime = actualTime.plusMinutes(minutesInterval);
        }

        DatesEntity datesEntity = new DatesEntity();
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
        }
        throw new DoctorNotFoundException("Podany doktor nie istnieje!");
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
