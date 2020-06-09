package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.exception.DataDuplicationException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.property.AppointmentReservationProperties;
import com.cogent.cogentappointment.esewa.repository.*;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentReservationLogService;
import com.cogent.cogentappointment.esewa.service.HospitalService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.APPOINTMENT_EXISTS;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.INVALID_APPOINTMENT_TIME;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.HospitalDepartmentDutyRosterMessages.HOSPITAL_DEPARTMENT_NOT_AVAILABLE_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.HospitalDepartmentDutyRosterMessages.HOSPITAL_DEPARTMENT_NOT_AVAILABLE_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDepartmentReservationLog.APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT_BILLING_MODE_INFO;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentReservationLogUtils.parseToAppointmentHospitalDepartmentReservation;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentUtils.validateIfRequestedAppointmentTimeIsValid;
import static com.cogent.cogentappointment.esewa.utils.AppointmentUtils.validateIfRequestIsBeforeCurrentDateTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti on 04/06/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentReservationLogServiceImpl implements
        AppointmentHospitalDepartmentReservationLogService {

    private final AppointmentHospitalDepartmentReservationLogRepository appointmentHospitalDepartmentReservationLogRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentReservationProperties reservationProperties;

    private final HospitalService hospitalService;

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    private final HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository;

    private final HospitalDepartmentRoomInfoRepository hospitalDepartmentRoomInfoRepository;

    private final HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository;

    private final HospitalDeptWeekDaysDutyRosterRepository hospitalDeptWeekDaysDutyRosterRepository;

    private final HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository;

    public AppointmentHospitalDepartmentReservationLogServiceImpl(
            AppointmentHospitalDepartmentReservationLogRepository appointmentHospitalDepartmentReservationLogRepository,
            AppointmentRepository appointmentRepository,
            AppointmentReservationProperties reservationProperties,
            HospitalService hospitalService,
            HospitalDepartmentRepository hospitalDepartmentRepository,
            HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository,
            HospitalDepartmentRoomInfoRepository hospitalDepartmentRoomInfoRepository,
            HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository,
            HospitalDeptWeekDaysDutyRosterRepository hospitalDeptWeekDaysDutyRosterRepository,
            HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository) {
        this.appointmentHospitalDepartmentReservationLogRepository = appointmentHospitalDepartmentReservationLogRepository;
        this.appointmentRepository = appointmentRepository;
        this.reservationProperties = reservationProperties;
        this.hospitalService = hospitalService;
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
        this.hospitalDepartmentBillingModeInfoRepository = hospitalDepartmentBillingModeInfoRepository;
        this.hospitalDepartmentRoomInfoRepository = hospitalDepartmentRoomInfoRepository;
        this.hospitalDeptDutyRosterOverrideRepository = hospitalDeptDutyRosterOverrideRepository;
        this.hospitalDeptWeekDaysDutyRosterRepository = hospitalDeptWeekDaysDutyRosterRepository;
        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
    }

    /*   VALIDATE REQUEST INFO :
    *   A. VALIDATE IF REQUESTED DATE AND TIME IS BEFORE CURRENT DATE AND TIME.
    *   B. VALIDATE IF ANY OTHER APPOINTMENT EXISTS ON THE SAME CRITERIA
    *   C. VALIDATE IF REQUESTED APPOINTMENT TIME LIES BETWEEN HOSPITAL DEPARTMENT DUTY ROSTER TIME SCHEDULES
    *
    *   SAVE IN APPOINTMENT HOSPITAL DEPARTMENT RESERVATION LOG ONLY IF IT HAS NOT BEEN SAVED BEFORE
    *   (SAME HOSPITAL DEPARTMENT, HOSPITAL, APPOINTMENT DATE/TIME */
    @Override
    public Long saveAppointmentHospitalDeptReservationLog(AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG);

        validateRequestedAppointmentInfo(requestDTO);

        Long appointmentReservationLogId = fetchAppointmentReservationLogId(requestDTO);

        if (Objects.isNull(appointmentReservationLogId))
            appointmentReservationLogId = save(requestDTO);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG,
                getDifferenceBetweenTwoTime(startTime));

        return appointmentReservationLogId;
    }

    /*SCHEDULER - 2 MINS
    * DELETE - 5 MINS*/
    @Override
    public void deleteExpiredAppointmentHospitalDeptReservation() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG);

        List<AppointmentHospitalDepartmentReservationLog> appointmentReservations =
                appointmentHospitalDepartmentReservationLogRepository.fetchAppointmentHospitalDepartmentReservationLog();

        appointmentReservations.forEach(appointmentReservation -> {

            long expiryDate = appointmentReservation.getCreatedDate().getTime() +
                    TimeUnit.MINUTES.toMillis(Long.parseLong(reservationProperties.getDeleteIntervalInMinutes()));

            long currentDateInMillis = new Date().getTime();

            if (expiryDate < currentDateInMillis)
                appointmentHospitalDepartmentReservationLogRepository.delete(appointmentReservation);
        });

        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG,
                getDifferenceBetweenTwoTime(startTime));
    }

    private Long save(AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {

        Hospital hospital = hospitalService.fetchActiveHospital(requestDTO.getHospitalId());

        HospitalDepartment hospitalDepartment = fetchHospitalDepartment(
                requestDTO.getHospitalDepartmentId(), requestDTO.getHospitalId()
        );

        HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo =
                fetchHospitalDepartmentBillingModeInfo(requestDTO.getHospitalDepartmentBillingModeId(),
                        requestDTO.getHospitalDepartmentId());

        HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo =
                Objects.isNull(requestDTO.getHospitalDepartmentRoomInfoId())
                        ? null
                        : fetchHospitalDepartmentRoomInfo(requestDTO.getHospitalDepartmentRoomInfoId(),
                        requestDTO.getHospitalDepartmentId());

        AppointmentHospitalDepartmentReservationLog appointmentReservationLog =
                parseToAppointmentHospitalDepartmentReservation(requestDTO,
                        hospital, hospitalDepartment, hospitalDepartmentBillingModeInfo, hospitalDepartmentRoomInfo);

        appointmentHospitalDepartmentReservationLogRepository.save(appointmentReservationLog);

        return appointmentReservationLog.getId();
    }

    private Long fetchAppointmentReservationLogId(AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {
        return appointmentHospitalDepartmentReservationLogRepository.fetchAppointmentHospitalDeptReservationLogId(requestDTO);
    }

    private void validateRequestedAppointmentInfo(AppointmentHospitalDeptFollowUpRequestDTO appointmentInfo) {

        validateIfRequestIsBeforeCurrentDateTime(
                appointmentInfo.getAppointmentDate(), appointmentInfo.getAppointmentTime());

        validateIfParentAppointmentExists(appointmentInfo);

        HospitalDeptDutyRosterTimeResponseTO hospitalDeptDutyRosterTimeResponseTO =
                fetchHospitalDeptDutyRoster(appointmentInfo);

        boolean isTimeValid = validateIfRequestedAppointmentTimeIsValid(
                hospitalDeptDutyRosterTimeResponseTO, appointmentInfo.getAppointmentTime());

        if (!isTimeValid) {
            log.error(INVALID_APPOINTMENT_TIME, convert24HourTo12HourFormat(appointmentInfo.getAppointmentTime()));
            throw new NoContentFoundException(String.format(INVALID_APPOINTMENT_TIME,
                    convert24HourTo12HourFormat(appointmentInfo.getAppointmentTime())));
        }
    }

    /*VALIDATE IF APPOINTMENT ALREADY EXISTS ON SELECTED DATE AND TIME */
    private void validateIfParentAppointmentExists(AppointmentHospitalDeptFollowUpRequestDTO appointmentInfo) {

        Long appointmentCount = appointmentRepository.validateIfAppointmentExistsDeptWise(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getAppointmentTime(),
                appointmentInfo.getHospitalDepartmentId(),
                appointmentInfo.getHospitalDepartmentRoomInfoId()
        );

        validateAppointmentExists(appointmentCount, appointmentInfo.getAppointmentTime());
    }

    private void validateAppointmentExists(Long appointmentCount, String appointmentTime) {
        if (appointmentCount.intValue() > 0) {
            log.error(APPOINTMENT_EXISTS, convert24HourTo12HourFormat(appointmentTime));
            throw new DataDuplicationException(String.format(APPOINTMENT_EXISTS,
                    convert24HourTo12HourFormat(appointmentTime)));
        }
    }

    /*FETCH HOSPITAL DEPARTMENT DUTY ROSTER FOR SELECTED DATE, HOSPITAL DEPARTMENT AND ROOM (IF APPLICABLE)
 * IF HOSPITAL DEPARTMENT DAY OFF = 'Y', THEN HOSPITAL DEPARTMENT IS NOT AVAILABLE AND CANNOT TAKE AN APPOINTMENT*/
    private HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRoster(
            AppointmentHospitalDeptFollowUpRequestDTO appointmentInfo) {

        HospitalDeptDutyRosterTimeResponseTO hospitalDeptDutyRoster = fetchHospitalDeptDutyRoster(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getHospitalDepartmentId(),
                appointmentInfo.getHospitalDepartmentRoomInfoId()
        );

        if (hospitalDeptDutyRoster.getDayOffStatus().equals(YES)) {
            Date appointmentDate = utilDateToSqlDate(appointmentInfo.getAppointmentDate());
            log.error(String.format(HOSPITAL_DEPARTMENT_NOT_AVAILABLE_DEBUG_MESSAGE, appointmentDate));
            throw new NoContentFoundException(
                    String.format(HOSPITAL_DEPARTMENT_NOT_AVAILABLE_MESSAGE, appointmentDate),
                    String.format(HOSPITAL_DEPARTMENT_NOT_AVAILABLE_DEBUG_MESSAGE, appointmentDate));
        }

        return hospitalDeptDutyRoster;
    }

    private HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRoster(
            Date appointmentDate,
            Long hospitalDepartmentId,
            Long hospitalDepartmentRoomInfoId) {

        HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster =
                Objects.isNull(hospitalDepartmentRoomInfoId)
                        ? fetchHospitalDepartmentDutyRosterWithoutRoom(appointmentDate, hospitalDepartmentId)
                        : fetchHospitalDepartmentDutyRosterWithRoom(
                        appointmentDate, hospitalDepartmentId, hospitalDepartmentRoomInfoId);

        if (hospitalDepartmentDutyRoster.getHasOverrideDutyRoster().equals(YES)) {
            HospitalDeptDutyRosterTimeResponseTO overrideRoster =
                    hospitalDeptDutyRosterOverrideRepository.fetchHospitalDeptDutyRosterOverrideTimeInfo(
                            hospitalDepartmentDutyRoster.getId(),
                            appointmentDate, hospitalDepartmentId,
                            hospitalDepartmentRoomInfoId);

            if (!Objects.isNull(overrideRoster))
                return overrideRoster;
            else
                return hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                        hospitalDepartmentDutyRoster.getId(), appointmentDate);

        } else {
            return hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                    hospitalDepartmentDutyRoster.getId(), appointmentDate);
        }
    }

    private HospitalDepartment fetchHospitalDepartment(Long hospitalDepartmentId, Long hospitalId) {
        return hospitalDepartmentRepository.fetchActiveByIdAndHospitalId(hospitalDepartmentId, hospitalId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT, id);
        throw new NoContentFoundException(HospitalDepartment.class, "id", id.toString());
    };

    private HospitalDepartmentBillingModeInfo fetchHospitalDepartmentBillingModeInfo(
            Long hospitalDepartmentBillingModeId, Long hospitalDepartmentId) {

        return hospitalDepartmentBillingModeInfoRepository.fetchByIdAndHospitalDepartmentId(
                hospitalDepartmentBillingModeId, hospitalDepartmentId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_CHARGE_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));
    }

    private HospitalDepartmentRoomInfo fetchHospitalDepartmentRoomInfo(Long hospitalDepartmentRoomInfoId,
                                                                       Long hospitalDepartmentId) {

        return hospitalDepartmentRoomInfoRepository.fetchHospitalDepartmentRoomInfo(
                hospitalDepartmentRoomInfoId, hospitalDepartmentId);
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_CHARGE_WITH_GIVEN_ID_NOT_FOUND =
            (hospitalDepartmentId) -> {
                log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_BILLING_MODE_INFO, hospitalDepartmentId);
                throw new NoContentFoundException(HospitalDepartmentBillingModeInfo.class, "hospitalDepartmentId",
                        hospitalDepartmentId.toString());
            };

    private HospitalDepartmentDutyRoster fetchHospitalDepartmentDutyRosterWithoutRoom(Date appointmentDate,
                                                                                      Long hospitalDepartmentId) {
        return hospitalDeptDutyRosterRepository.fetchHospitalDeptDutyRosterWithoutRoom(
                appointmentDate, hospitalDepartmentId);
    }

    private HospitalDepartmentDutyRoster fetchHospitalDepartmentDutyRosterWithRoom(Date appointmentDate,
                                                                                   Long hospitalDepartmentId,
                                                                                   Long hospitalDepartmentRoomInfoId) {
        return hospitalDeptDutyRosterRepository.fetchHospitalDeptDutyRosterWithRoom(
                appointmentDate, hospitalDepartmentId, hospitalDepartmentRoomInfoId);
    }
}
