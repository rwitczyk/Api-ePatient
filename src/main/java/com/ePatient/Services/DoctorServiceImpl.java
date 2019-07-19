package com.ePatient.Services;

import com.ePatient.Daos.DoctorDao;
import com.ePatient.Entities.Doctor;
import com.ePatient.Exceptions.DoctorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService{

    private DoctorDao doctorDao;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    @Override
    public void addDoctor(Doctor doctor) {
        doctorDao.save(doctor);
    }

    @Override
    public void deleteDoctorById(int doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        if(doctor != null) {
            doctorDao.delete(doctor);
        }
        throw new DoctorNotFoundException("Podany doktor nie istnieje!");
    }

    @Override
    public Doctor getDoctorById(int doctorId) {
        Doctor doctor = doctorDao.getDoctorByDoctorId(doctorId);
        if(doctor != null) {
            return doctor;
        }

        throw new DoctorNotFoundException("Podany doktor nie istnieje");
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }
}
