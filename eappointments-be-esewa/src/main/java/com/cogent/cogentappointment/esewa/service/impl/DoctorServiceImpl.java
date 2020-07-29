package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.DoctorRepository;
import com.cogent.cogentappointment.esewa.service.DoctorService;
import com.cogent.cogentappointment.persistence.model.Doctor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 2019-09-29
 */
@Service
@Transactional
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor fetchActiveDoctorByIdAndHospitalId(Long id, Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = doctorRepository.fetchActiveDoctorByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return doctor;
    }

    @Override
    public Double fetchDoctorAppointmentCharge(Long doctorId, Long hospitalId) {
        return doctorRepository.fetchDoctorAppointmentCharge(doctorId, hospitalId);
    }

    @Override
    public Double fetchDoctorFollowupAppointmentCharge(Long doctorId, Long hospitalId) {
        return doctorRepository.fetchDoctorAppointmentFollowUpCharge(doctorId, hospitalId);
    }

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID,DOCTOR,id);
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };
}


