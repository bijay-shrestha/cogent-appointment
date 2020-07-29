package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.persistence.model.Doctor;


import java.util.List;

/**
 * @author smriti on 2019-09-29
 */
public interface DoctorService {

    Doctor fetchActiveDoctorByIdAndHospitalId(Long id, Long hospitalId);

    Double fetchDoctorAppointmentCharge(Long doctorId, Long hospitalId);

    Double fetchDoctorFollowupAppointmentCharge(Long doctorId, Long hospitalId);
}
