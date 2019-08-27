package com.ePatient.Services;

import com.ePatient.Entities.Dates;
import com.ePatient.Entities.Doctor;
import com.ePatient.Exceptions.DoctorNotFoundException;
import com.ePatient.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService{

    private DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void addDoctor(Doctor doctor) {
        List<Dates> list = new ArrayList<>();

        for (int i=1 ; i< 60; i++) {
            list.add(prepareOneDay(LocalDate.now()));
            list.add(prepareOneDay(LocalDate.now().plusDays(i)));
        }

        doctor.setDays(list);
        doctorRepository.save(doctor);
    }

    private Dates prepareOneDay(LocalDate date) {
        Map<String,Boolean> listOfHours = new HashMap<>();
        listOfHours.put("8:00", false);
        listOfHours.put("8:30", false);
        listOfHours.put("9:00", false);
        listOfHours.put("9:30", false);
        listOfHours.put("10:00", false);
        listOfHours.put("10:30", false);
        listOfHours.put("11:00", false);
        listOfHours.put("11:30", false);
        listOfHours.put("12:00", false);
        listOfHours.put("12:30", false);
        listOfHours.put("13:00", false);
        listOfHours.put("13:30", false);
        listOfHours.put("14:00", false);
        listOfHours.put("14:30", false);
        listOfHours.put("15:00", false);
        listOfHours.put("15:30", false);
        listOfHours.put("16:00", false);
        listOfHours.put("16:30", false);

        Dates dates = new Dates();
        dates.setDate(date);
        dates.setListOfHours(listOfHours);
        return dates;
    }

    @Override
    public void deleteDoctorById(int doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        if(doctor != null) {
            doctorRepository.delete(doctor);
        }
        throw new DoctorNotFoundException("Podany doktor nie istnieje!");
    }

    @Override
    public Doctor getDoctorById(int doctorId) {
        Doctor doctor = doctorRepository.getDoctorByDoctorId(doctorId);
        if(doctor != null) {
            return doctor;
        }

        throw new DoctorNotFoundException("Podany doktor nie istnieje");
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
