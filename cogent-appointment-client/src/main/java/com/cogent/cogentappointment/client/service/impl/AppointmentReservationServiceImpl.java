package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.repository.AppointmentReservationRepository;
import com.cogent.cogentappointment.client.service.AppointmentReservationService;
import com.cogent.cogentappointment.client.service.DoctorService;
import com.cogent.cogentappointment.client.service.HospitalService;
import com.cogent.cogentappointment.client.service.SpecializationService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentReservationLog.APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.client.utils.AppointmentReservationUtils.parseToAppointmentReservation;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 18/02/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentReservationServiceImpl implements AppointmentReservationService {

    private final AppointmentReservationRepository appointmentReservationRepository;

    private final DoctorService doctorService;

    private final SpecializationService specializationService;

    private final HospitalService hospitalService;

    public AppointmentReservationServiceImpl(AppointmentReservationRepository appointmentReservationRepository,
                                             DoctorService doctorService,
                                             SpecializationService specializationService,
                                             HospitalService hospitalService) {
        this.appointmentReservationRepository = appointmentReservationRepository;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.hospitalService = hospitalService;
    }

    @Override
    public void save(AppointmentFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_RESERVATION_LOG);

        AppointmentReservation appointmentReservation =
                parseToAppointmentReservation(requestDTO,
                        fetchHospital(requestDTO.getHospitalId()),
                        fetchDoctor(requestDTO.getDoctorId()),
                        fetchSpecialization(requestDTO.getSpecializationId()));

        save(appointmentReservation);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));
    }

    private void save(AppointmentReservation appointmentReservation) {
        appointmentReservationRepository.save(appointmentReservation);
    }

    private Doctor fetchDoctor(Long doctorId) {
        return doctorService.fetchActiveDoctorById(doctorId);
    }

    private Specialization fetchSpecialization(Long specializationId) {
        return specializationService.fetchActiveSpecializationById(specializationId);
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

}
