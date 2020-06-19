package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalFollowUpResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.AppointmentHospitalDepartmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.esewa.repository.HospitalDepartmentBillingModeInfoRepository;
import com.cogent.cogentappointment.esewa.repository.HospitalRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentFollowUpTrackerService;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentFollowUpRequestLogService;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentReservationLogService;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDepartmentFollowUpTrackerLog.APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentFollowUpTrackerUtils.parseAppointmentHospitalDeptFollowUpResponseDTO;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentFollowUpTrackerUtils.parseResponseStatus;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti on 16/02/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentFollowUpTrackerServiceImpl implements AppointmentHospitalDepartmentFollowUpTrackerService {

    private final AppointmentHospitalDepartmentFollowUpTrackerRepository appointmentHospitalDepartmentFollowUpTrackerRepository;

    private final HospitalRepository hospitalRepository;

    private final HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository;

    private final AppointmentHospitalDepartmentReservationLogService appointmentHospitalDepartmentReservationLogService;

    private final AppointmentHospitalDepartmentFollowUpRequestLogService appointmentFollowUpRequestLogService;

    public AppointmentHospitalDepartmentFollowUpTrackerServiceImpl(
            AppointmentHospitalDepartmentFollowUpTrackerRepository appointmentHospitalDepartmentFollowUpTrackerRepository,
            HospitalRepository hospitalRepository,
            HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository,
            AppointmentHospitalDepartmentReservationLogService appointmentHospitalDepartmentReservationLogService,
            AppointmentHospitalDepartmentFollowUpRequestLogService appointmentFollowUpRequestLogService) {
        this.appointmentHospitalDepartmentFollowUpTrackerRepository = appointmentHospitalDepartmentFollowUpTrackerRepository;
        this.hospitalRepository = hospitalRepository;
        this.hospitalDepartmentBillingModeInfoRepository = hospitalDepartmentBillingModeInfoRepository;
        this.appointmentHospitalDepartmentReservationLogService = appointmentHospitalDepartmentReservationLogService;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
    }

    @Override
    public AppointmentHospitalDeptFollowUpResponseDTO fetchAppointmentHospitalDeptFollowUpDetails
            (AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER);

        /*TEMPORARILY HOLD SELECTED TIME SLOT
        * PERSIST IN TABLE ONLY IF APPOINTMENT HAS NOT BEEN PREVIOUSLY RESERVED FOR
        * SELECTED HOSPITAL DEPARTMENT, ROOM(IF APPLICABLE), DATE AND TIME */
        Long savedAppointmentReservationId =
                appointmentHospitalDepartmentReservationLogService.saveAppointmentHospitalDeptReservationLog(requestDTO);

        AppointmentHospitalDepartmentFollowUpTracker appointmentHospitalDeptFollowUpTracker =
                appointmentHospitalDepartmentFollowUpTrackerRepository.fetchAppointmentHospitalDeptFollowUpTracker(
                        requestDTO.getHospitalId(),
                        requestDTO.getHospitalDepartmentId(),
                        requestDTO.getPatientId()
                );

        Double hospitalRefundPercentage = fetchHospitalRefundPercentage(requestDTO.getHospitalId());

        AppointmentHospitalDeptFollowUpResponseDTO responseDTO;

        if (Objects.isNull(appointmentHospitalDeptFollowUpTracker))

            /*THIS IS NORMAL APPOINTMENT AND APPOINTMENT CHARGE = HOSPITAL DEPARTMENT APPOINTMENT CHARGE*/
            responseDTO = parseHospitalDeptAppointmentCharge(
                    requestDTO.getHospitalDepartmentBillingModeId(),
                    requestDTO.getHospitalDepartmentId(),
                    savedAppointmentReservationId,
                    hospitalRefundPercentage
            );
        else
             /*THIS IS FOLLOW UP APPOINTMENT CASE AND APPOINTMENT CHARGE = HOSPITAL DEPARTMENT FOLLOW UP APPOINTMENT CHARGE*/
            responseDTO = parseHospitalDepartmentAppointmentFollowUpCharge(
                    appointmentHospitalDeptFollowUpTracker,
                    requestDTO,
                    savedAppointmentReservationId,
                    hospitalRefundPercentage
            );

        parseResponseStatus(responseDTO);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER,
                getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public Long fetchByParentAppointmentId(Long parentAppointmentId) {
        return appointmentHospitalDepartmentFollowUpTrackerRepository.fetchByParentAppointmentId(parentAppointmentId)
                .orElseThrow(() -> new NoContentFoundException(AppointmentFollowUpTracker.class));
    }

    /* TO BE A FOLLOW UP APPOINTMENT:
     1. IF REMAINING NUMBER OF FOLLOW UPS IN AppointmentHospitalDepartmentFollowUpTracker > 0
         (SUPPOSE INITIALLY IT IS 3(AS PER HOSPITAL). NOW WHEN USER CHECKS IN, THAT COUNT IS DECREMENTED BY 1
         ie NOW ITS 2 AND DECREMENTS TILL 0)

    2. IF FOLLOW UP REQUEST COUNT IN AppointmentFollowUpRequestLog < ALLOWED numberOfFollowUps IN Hospital
    (WHEN FIRST APPOINTMENT IS APPROVED ->
        PERSIST IN AppointmentHospitalDepartmentFollowUpTracker AND
        AppointmentFollowUpRequestLog WITH REQUEST COUNT AS 0.
    WHEN CONSECUTIVE FOLLOW UP APPOINTMENT IS TAKEN, REQUEST COUNT IS INCREMENTED BY 1 )

    3. IF REQUESTED APPOINTMENT DATE HAS NOT EXPIRED WHERE
         EXPIRY DATE = APPOINTMENT APPROVED DATE + FOLLOW UP INTERVAL DAYS (FROM Hospital)

    IF ALL THREE CONDITIONS ARE SATISFIED,
    THEN
        IT IS FOLLOW UP APPOINTMENT WITH APPOINTMENT CHARGE AS HOSPITAL DEPARTMENT FOLLOW UP APPOINTMENT CHARGE

    ELSE
        IT IS NORMAL APPOINTMENT WITH APPOINTMENT CHARGE AS HOSPITAL DEPARTMENT APPOINTMENT CHARGE
    */
    private AppointmentHospitalDeptFollowUpResponseDTO parseHospitalDepartmentAppointmentFollowUpCharge(
            AppointmentHospitalDepartmentFollowUpTracker appointmentFollowUpTracker,
            AppointmentHospitalDeptFollowUpRequestDTO requestDTO,
            Long savedAppointmentReservationId,
            Double hospitalRefundPercentage) {

        if (appointmentFollowUpTracker.getRemainingNumberOfFollowUps() <= 0)
            /*NORMAL APPOINTMENT*/
            return parseHospitalDeptAppointmentCharge(
                    requestDTO.getHospitalDepartmentBillingModeId(),
                    requestDTO.getHospitalDepartmentId(), savedAppointmentReservationId,
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
                return parseHospitalDeptAppointmentFollowUpCharge(
                        requestDTO.getHospitalDepartmentBillingModeId(),
                        requestDTO.getHospitalDepartmentId(),
                        savedAppointmentReservationId,
                        hospitalRefundPercentage,
                        appointmentFollowUpTracker.getParentAppointmentId()
                );
            else
                 /*NORMAL APPOINTMENT*/
                return parseHospitalDeptAppointmentCharge(
                        requestDTO.getHospitalDepartmentBillingModeId(),
                        requestDTO.getHospitalDepartmentId(), savedAppointmentReservationId,
                        hospitalRefundPercentage
                );

        } else {
             /*NORMAL APPOINTMENT*/
            return parseHospitalDeptAppointmentCharge(
                    requestDTO.getHospitalDepartmentBillingModeId(),
                    requestDTO.getHospitalDepartmentId(), savedAppointmentReservationId,
                    hospitalRefundPercentage
            );
        }
    }

    private AppointmentHospitalDeptFollowUpResponseDTO parseHospitalDeptAppointmentCharge(
            Long hospitalDepartmentBillingModeId, Long hospitalDepartmentId,
            Long savedAppointmentReservationId,
            Double hospitalRefundPercentage) {

        Double appointmentCharge = hospitalDepartmentBillingModeInfoRepository.fetchHospitalDeptAppointmentCharge(
                hospitalDepartmentBillingModeId, hospitalDepartmentId);

        return parseAppointmentHospitalDeptFollowUpResponseDTO(
                NO, appointmentCharge, null, savedAppointmentReservationId, hospitalRefundPercentage
        );
    }

    private AppointmentHospitalDeptFollowUpResponseDTO parseHospitalDeptAppointmentFollowUpCharge(
            Long hospitalDepartmentBillingModeId,
            Long hospitalDepartmentId,
            Long savedAppointmentReservationId,
            Double hospitalRefundPercentage,
            Long parentAppointmentId) {

        Double followUpCharge =
                hospitalDepartmentBillingModeInfoRepository.fetchHospitalDeptAppointmentFollowUpCharge(
                        hospitalDepartmentBillingModeId, hospitalDepartmentId);

        return parseAppointmentHospitalDeptFollowUpResponseDTO(
                YES, followUpCharge, parentAppointmentId, savedAppointmentReservationId, hospitalRefundPercentage
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
