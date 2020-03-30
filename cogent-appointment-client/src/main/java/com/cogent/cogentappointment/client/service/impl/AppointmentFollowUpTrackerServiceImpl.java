package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalFollowUpResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.client.repository.DoctorRepository;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.service.AppointmentFollowUpRequestLogService;
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

    private final AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService;

    public AppointmentFollowUpTrackerServiceImpl(AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository,
                                                 HospitalRepository hospitalRepository,
                                                 DoctorRepository doctorRepository,
                                                 AppointmentReservationService appointmentReservationService,
                                                 AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService) {
        this.appointmentFollowUpTrackerRepository = appointmentFollowUpTrackerRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentReservationService = appointmentReservationService;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
    }

    @Override
    public AppointmentFollowUpResponseDTO fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        /*TEMPORARILY HOLD SELECTED TIME SLOT*/
        Long savedAppointmentReservationId = appointmentReservationService.save(requestDTO);

        AppointmentFollowUpTracker appointmentFollowUpTracker =
                appointmentFollowUpTrackerRepository.fetchAppointmentFollowUpTracker(
                        requestDTO.getPatientId(), requestDTO.getDoctorId(),
                        requestDTO.getSpecializationId(), requestDTO.getHospitalId()
                );

        AppointmentFollowUpResponseDTO responseDTO;

        if (Objects.isNull(appointmentFollowUpTracker))

            /*THIS IS NORMAL APPOINTMENT AND APPOINTMENT CHARGE = DOCTOR APPOINTMENT CHARGE*/
            responseDTO = parseDoctorAppointmentCharge(
                    requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId);
        else
             /*THIS IS FOLLOW UP APPOINTMENT CASE AND APPOINTMENT CHARGE = DOCTOR FOLLOW UP APPOINTMENT CHARGE*/
            responseDTO = parseDoctorAppointmentFollowUpCharge(
                    appointmentFollowUpTracker, requestDTO, savedAppointmentReservationId);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentFollowUpTracker save(Long parentAppointmentId,
                                           Hospital hospital,
                                           Doctor doctor,
                                           Specialization specialization,
                                           Patient patient) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        Integer numberOfFollowUps = hospitalRepository.fetchHospitalFollowUpCount(hospital.getId());

        AppointmentFollowUpTracker appointmentFollowUpTracker = save(parseToAppointmentFollowUpTracker(
                parentAppointmentId, numberOfFollowUps,
                doctor, specialization, patient, hospital)
        );

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return appointmentFollowUpTracker;
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

    private AppointmentFollowUpTracker save(AppointmentFollowUpTracker followUpTracker) {
        return appointmentFollowUpTrackerRepository.save(followUpTracker);
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

    @Override
    public Long fetchAppointmentFollowUpTrackerByParentAppointmentId(Long parentAppointmentId) {

        Long appointmentFollowUpTrackerId =
                appointmentFollowUpTrackerRepository.fetchByParentAppointmentId(parentAppointmentId)
                        .orElseThrow(() -> new NoContentFoundException(AppointmentFollowUpTracker.class));
        return null;
    }

    /* TO BE A FOLLOW UP APPOINTMENT:
     1. IF REMAINING NUMBER OF FOLLOW UPS IN AppointmentFollowUpTracker > 0
         (SUPPOSE INITIALLY IT IS 3(AS PER HOSPITAL). NOW WHEN USER CHECKS IN, THAT COUNT IS DECREMENTED BY 1
         ie NOW ITS 2 AND DECREMENTS TILL 0)

    2. IF FOLLOW UP REQUEST COUNT IN AppointmentFollowUpRequestLogConstant < ALLOWED numberOfFollowUps IN Hospital
    (WHEN FIRST APPOINTMENT IS APPROVED ->
        PERSIST IN AppointmentFollowUpTracker AND
        AppointmentFollowUpRequestLogConstant WITH REQUEST COUNT AS 0.
    WHEN CONSECUTIVE FOLLOW UP APPOINTMENT IS TAKEN, REQUEST COUNT IS INCREMENTED BY 1 )

    3. IF REQUESTED APPOINTMENT DATE HAS NOT EXPIRED WHERE
         EXPIRY DATE = APPOINTMENT APPROVED DATE + FOLLOW UP INTERVAL DAYS (FROM Hospital)

    IF ALL THREE CONDITIONS ARE SATISFIED,
    THEN
        IT IS FOLLOW UP APPOINTMENT WITH APPOINTMENT CHARGE AS DOCTOR FOLLOW UP APPOINTMENT CHARGE (FROM Doctor SETUP)
    ELSE
        IT IS NORMAL APPOINTMENT WITH APPOINTMENT CHARGE AS DOCTOR APPOINTMENT CHARGE (FROM Doctor SETUP)
    */
    private AppointmentFollowUpResponseDTO parseDoctorAppointmentFollowUpCharge(
            AppointmentFollowUpTracker appointmentFollowUpTracker,
            AppointmentFollowUpRequestDTO requestDTO,
            Long savedAppointmentReservationId) {

        if (appointmentFollowUpTracker.getRemainingNumberOfFollowUps() <= 0)
            /*NORMAL APPOINTMENT*/
            return parseDoctorAppointmentCharge
                    (requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId);

        Integer followUpRequestCount =
                appointmentFollowUpRequestLogService.fetchRequestCountByFollowUpTrackerId
                        (appointmentFollowUpTracker.getId());

        HospitalFollowUpResponseDTO followUpDetails =
                hospitalRepository.fetchFollowUpDetails(requestDTO.getHospitalId());

        if (followUpRequestCount < followUpDetails.getNumberOfFollowUps()) {
            /*FOLLOW UP APPOINTMENT*/
            Date requestedDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
            Date expiryDate = utilDateToSqlDate(addDays(
                    appointmentFollowUpTracker.getAppointmentApprovedDate(),
                    followUpDetails.getFollowUpIntervalDays()));

            if (isAppointmentActive(requestedDate, expiryDate))
                  /*FOLLOW UP APPOINTMENT*/
                return parseDoctorAppointmentFollowUpCharge(
                        requestDTO.getDoctorId(), requestDTO.getHospitalId(),
                        appointmentFollowUpTracker.getParentAppointmentId(), savedAppointmentReservationId
                );
            else
                 /*NORMAL APPOINTMENT*/
                return parseDoctorAppointmentCharge
                        (requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId);

        } else {
             /*NORMAL APPOINTMENT*/
            return parseDoctorAppointmentCharge
                    (requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId);
        }
    }

    private AppointmentFollowUpResponseDTO parseDoctorAppointmentCharge(Long doctorId,
                                                                        Long hospitalId,
                                                                        Long savedAppointmentReservationId) {

        Double doctorAppointmentCharge = doctorRepository.fetchDoctorAppointmentCharge(
                doctorId, hospitalId);

        return parseToAppointmentFollowUpResponseDTO(
                NO, doctorAppointmentCharge, null, savedAppointmentReservationId);
    }

    private AppointmentFollowUpResponseDTO parseDoctorAppointmentFollowUpCharge(Long doctorId,
                                                                                Long hospitalId,
                                                                                Long parentAppointmentId,
                                                                                Long savedAppointmentReservationId) {

        Double doctorFollowUpCharge = doctorRepository.fetchDoctorAppointmentFollowUpCharge(
                doctorId, hospitalId);

        return parseToAppointmentFollowUpResponseDTO(
                YES, doctorFollowUpCharge,
                parentAppointmentId, savedAppointmentReservationId
        );
    }

    private boolean isAppointmentActive(Date requestedDate, Date expiryDate) {

        return (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) < 0
                || (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) == 0;
    }


}
