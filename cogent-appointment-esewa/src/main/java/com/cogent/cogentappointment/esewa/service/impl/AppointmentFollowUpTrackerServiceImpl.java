package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.followup.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.followup.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.followup.AppointmentFollowUpResponseDTOWithStatus;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalFollowUpResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.esewa.repository.DoctorRepository;
import com.cogent.cogentappointment.esewa.repository.HospitalRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentFollowUpRequestLogService;
import com.cogent.cogentappointment.esewa.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.esewa.service.AppointmentReservationLogService;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.esewa.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpResponseDTO;
import static com.cogent.cogentappointment.esewa.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpResponseDTOWithStatus;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

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

    private final AppointmentReservationLogService appointmentReservationLogService;

    private final AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService;

    private final Validator validator;

    public AppointmentFollowUpTrackerServiceImpl(AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository,
                                                 HospitalRepository hospitalRepository,
                                                 DoctorRepository doctorRepository,
                                                 AppointmentReservationLogService appointmentReservationLogService,
                                                 AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService,
                                                 Validator validator) {
        this.appointmentFollowUpTrackerRepository = appointmentFollowUpTrackerRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentReservationLogService = appointmentReservationLogService;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
        this.validator = validator;
    }

    @Override
    public AppointmentFollowUpResponseDTOWithStatus fetchAppointmentFollowUpDetails(
            @Valid AppointmentFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        validateConstraintViolation(validator.validate(requestDTO));

        /*TEMPORARILY HOLD SELECTED TIME SLOT
        * PERSIST IN TABLE ONLY IF APPOINTMENT HAS NOT BEEN PREVIOUSLY RESERVED FOR
        * SELECTED DOCTOR, SPECIALIZATION, DATE AND TIME */
        Long savedAppointmentReservationId = appointmentReservationLogService.saveAppointmentReservationLog(requestDTO);

        AppointmentFollowUpTracker appointmentFollowUpTracker =
                appointmentFollowUpTrackerRepository.fetchAppointmentFollowUpTracker(
                        requestDTO.getPatientId(), requestDTO.getDoctorId(),
                        requestDTO.getSpecializationId(), requestDTO.getHospitalId()
                );

        Double hospitalRefundPercentage = fetchHospitalRefundPercentage(requestDTO.getHospitalId());

        AppointmentFollowUpResponseDTO responseDTO;

        if (Objects.isNull(appointmentFollowUpTracker))

            /*THIS IS NORMAL APPOINTMENT AND APPOINTMENT CHARGE = DOCTOR APPOINTMENT CHARGE*/
            responseDTO = parseDoctorAppointmentCharge(
                    requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId,
                    hospitalRefundPercentage
            );
        else
             /*THIS IS FOLLOW UP APPOINTMENT CASE AND APPOINTMENT CHARGE = DOCTOR FOLLOW UP APPOINTMENT CHARGE*/
            responseDTO = parseDoctorAppointmentFollowUpCharge(
                    appointmentFollowUpTracker, requestDTO, savedAppointmentReservationId, hospitalRefundPercentage
            );

        AppointmentFollowUpResponseDTOWithStatus responseDTOWithStatus =
                parseToAppointmentFollowUpResponseDTOWithStatus(responseDTO);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return responseDTOWithStatus;
    }


    @Override
    public Long fetchByParentAppointmentId(Long parentAppointmentId) {
        return appointmentFollowUpTrackerRepository.fetchByParentAppointmentId(parentAppointmentId)
                .orElseThrow(() -> new NoContentFoundException(AppointmentFollowUpTracker.class));
    }

    /* TO BE A FOLLOW UP APPOINTMENT:
     1. IF REMAINING NUMBER OF FOLLOW UPS IN AppointmentFollowUpTracker > 0
         (SUPPOSE INITIALLY IT IS 3(AS PER HOSPITAL). NOW WHEN USER CHECKS IN, THAT COUNT IS DECREMENTED BY 1
         ie NOW ITS 2 AND DECREMENTS TILL 0)

    2. IF FOLLOW UP REQUEST COUNT IN AppointmentFollowUpRequestLog < ALLOWED numberOfFollowUps IN Hospital
    (WHEN FIRST APPOINTMENT IS APPROVED ->
        PERSIST IN AppointmentFollowUpTracker AND
        AppointmentFollowUpRequestLog WITH REQUEST COUNT AS 0.
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
            Long savedAppointmentReservationId,
            Double hospitalRefundPercentage) {

        if (appointmentFollowUpTracker.getRemainingNumberOfFollowUps() <= 0)
            /*NORMAL APPOINTMENT*/
            return parseDoctorAppointmentCharge(
                    requestDTO.getDoctorId(), requestDTO.getHospitalId(), savedAppointmentReservationId,
                    hospitalRefundPercentage
            );

        Integer followUpRequestCount = appointmentFollowUpRequestLogService.fetchRequestCountByFollowUpTrackerId
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
                        appointmentFollowUpTracker.getParentAppointmentId(),
                        savedAppointmentReservationId, hospitalRefundPercentage
                );
            else
                 /*NORMAL APPOINTMENT*/
                return parseDoctorAppointmentCharge(
                        requestDTO.getDoctorId(), requestDTO.getHospitalId(),
                        savedAppointmentReservationId, hospitalRefundPercentage
                );

        } else {
             /*NORMAL APPOINTMENT*/
            return parseDoctorAppointmentCharge(
                    requestDTO.getDoctorId(), requestDTO.getHospitalId(),
                    savedAppointmentReservationId, hospitalRefundPercentage
            );
        }
    }

    private AppointmentFollowUpResponseDTO parseDoctorAppointmentCharge(Long doctorId,
                                                                        Long hospitalId,
                                                                        Long savedAppointmentReservationId,
                                                                        Double hospitalRefundPercentage) {

        Double doctorAppointmentCharge = doctorRepository.fetchDoctorAppointmentCharge(
                doctorId, hospitalId);

        return parseToAppointmentFollowUpResponseDTO(
                NO, doctorAppointmentCharge,
                null, savedAppointmentReservationId, hospitalRefundPercentage
        );
    }

    private AppointmentFollowUpResponseDTO parseDoctorAppointmentFollowUpCharge(Long doctorId,
                                                                                Long hospitalId,
                                                                                Long parentAppointmentId,
                                                                                Long savedAppointmentReservationId,
                                                                                Double hospitalRefundPercentage) {

        Double doctorFollowUpCharge = doctorRepository.fetchDoctorAppointmentFollowUpCharge(
                doctorId, hospitalId);

        return parseToAppointmentFollowUpResponseDTO(
                YES, doctorFollowUpCharge,
                parentAppointmentId, savedAppointmentReservationId, hospitalRefundPercentage
        );
    }

    private boolean isAppointmentActive(Date requestedDate, Date expiryDate) {

        return (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) < 0
                || (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) == 0;
    }

    private Double fetchHospitalRefundPercentage(Long hospitalId) {
        return hospitalRepository.fetchHospitalRefundPercentage(hospitalId)
                .orElseThrow(() -> new NoContentFoundException(Hospital.class, "hospitalId", hospitalId.toString()));
    }

}
