package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.client.repository.DoctorRepository;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.client.service.AppointmentReservationService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER_STATUS;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpTrackerUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static java.lang.reflect.Array.get;

/**
 * @author smriti on 16/02/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentFollowUpTrackerServiceImpl implements AppointmentFollowUpTrackerService {

    private final AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository;

    private final HospitalRepository hospitalRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentReservationService appointmentReservationService;

    public AppointmentFollowUpTrackerServiceImpl(AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository,
                                                 HospitalRepository hospitalRepository,
                                                 DoctorRepository doctorRepository,
                                                 AppointmentReservationService appointmentReservationService) {
        this.appointmentFollowUpTrackerRepository = appointmentFollowUpTrackerRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentReservationService = appointmentReservationService;
    }

    @Override
    public AppointmentFollowUpResponseDTO fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        /*TEMPORARILY HOLD SELECTED TIME SLOT*/
        Long savedAppointmentReservationId = appointmentReservationService.save(requestDTO);

        List<Object[]> followUpDetails = appointmentFollowUpTrackerRepository.fetchFollowUpDetails(
                requestDTO.getPatientId(),
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId()
        );

        AppointmentFollowUpResponseDTO responseDTO;

        if (followUpDetails.isEmpty())

            /*THIS IS NORMAL APPOINTMENT AND APPOINTMENT CHARGE = DOCTOR APPOINTMENT CHARGE*/
            responseDTO = parseDoctorAppointmentCharge(
                    requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId);
        else
             /*THIS IS FOLLOW UP APPOINTMENT CASE */
            responseDTO = parseDoctorAppointmentFollowUpCharge(followUpDetails, requestDTO, savedAppointmentReservationId);


        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void save(Long parentAppointmentId,
                     String parentAppointmentNumber,
                     Hospital hospital,
                     Doctor doctor,
                     Specialization specialization,
                     Patient patient) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        Integer numberOfFollowUps = hospitalRepository.fetchHospitalFollowUpCount(hospital.getId());

        save(parseToAppointmentFollowUpTracker(
                parentAppointmentId, parentAppointmentNumber, numberOfFollowUps,
                doctor, specialization, patient, hospital));

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateFollowUpTracker(Long parentAppointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        AppointmentFollowUpTracker followUpTracker =
                appointmentFollowUpTrackerRepository.fetchLatestAppointmentFollowUpTracker(parentAppointmentId);

        updateNumberOfFollowUps(followUpTracker);

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

    private void save(AppointmentFollowUpTracker followUpTracker) {
        appointmentFollowUpTrackerRepository.save(followUpTracker);
    }

    @Override
    public void updateFollowUpTrackerStatus() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS);

        List<AppointmentFollowUpTracker> followUpTrackers =
                appointmentFollowUpTrackerRepository.fetchActiveFollowUpTracker();

        followUpTrackers.forEach(followUpTracker -> {
            int intervalDays = hospitalRepository.fetchHospitalFollowUpIntervalDays(
                    followUpTracker.getHospitalId().getId());

            Date expiryDate = utilDateToSqlDate(
                    addDays(followUpTracker.getAppointmentApprovedDate(), intervalDays));

            Date currentDate = utilDateToSqlDate(new Date());

            if ((Objects.requireNonNull(expiryDate).compareTo(Objects.requireNonNull(currentDate))) < 0)
                followUpTracker.setStatus(INACTIVE);

        });

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS, getDifferenceBetweenTwoTime(startTime));
    }

    private boolean isAppointmentActive(Date requestedDate, Date expiryDate) {

        return (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) < 0
                || (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) == 0;
    }

    private AppointmentFollowUpResponseDTO parseDoctorAppointmentCharge(Long doctorId,
                                                                        Long hospitalId,
                                                                        Long savedAppointmentReservationId) {

        Double doctorAppointmentCharge = doctorRepository.fetchDoctorAppointmentCharge(
                doctorId, hospitalId);

        return parseToAppointmentFollowUpResponseDTO(NO, doctorAppointmentCharge, null,
                savedAppointmentReservationId);
    }

    private AppointmentFollowUpResponseDTO parseDoctorAppointmentFollowUpCharge(List<Object[]> followUpDetails,
                                                                                AppointmentFollowUpRequestDTO requestDTO,
                                                                                Long savedAppointmentReservationId) {

        Object[] followUpObject = followUpDetails.get(0);
        Long parentAppointmentId = (Long) get(followUpObject, 0);
        Date appointmentDate = (Date) get(followUpObject, 1);

        int intervalDays = hospitalRepository.fetchHospitalFollowUpIntervalDays(requestDTO.getHospitalId());

        Date requestedDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
        Date expiryDate = utilDateToSqlDate(addDays(appointmentDate, intervalDays));

        if (isAppointmentActive(requestedDate, expiryDate)) {

            Double doctorFollowUpCharge = doctorRepository.fetchDoctorAppointmentFollowUpCharge(
                    requestDTO.getDoctorId(), requestDTO.getHospitalId());

            return parseToAppointmentFollowUpResponseDTO(YES, doctorFollowUpCharge, parentAppointmentId,
                    savedAppointmentReservationId);
        } else {
            return parseDoctorAppointmentCharge
                    (requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId);
        }
    }
}
