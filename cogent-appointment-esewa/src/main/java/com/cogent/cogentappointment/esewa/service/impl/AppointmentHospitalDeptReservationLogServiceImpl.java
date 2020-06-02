package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.followup.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.esewa.exception.DataDuplicationException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.property.AppointmentReservationProperties;
import com.cogent.cogentappointment.esewa.repository.*;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDeptReservationLogService;
import com.cogent.cogentappointment.esewa.service.HospitalService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.APPOINTMENT_EXISTS;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_NOT_AVAILABLE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDepartmentReservationLog.APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti on 18/02/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDeptReservationLogServiceImpl implements
        AppointmentHospitalDeptReservationLogService {

    private final AppointmentHospitalDeptReservationLogRepository appointmentHospitalDeptReservationLogRepository;

    private final AppointmentRepository appointmentRepository;

    private final DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final AppointmentReservationProperties reservationProperties;

    private final HospitalService hospitalService;

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    public AppointmentHospitalDeptReservationLogServiceImpl(
            AppointmentHospitalDeptReservationLogRepository appointmentHospitalDeptReservationLogRepository,
            AppointmentRepository appointmentRepository,
            DoctorDutyRosterRepository doctorDutyRosterRepository,
            DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
            AppointmentReservationProperties reservationProperties,
            HospitalService hospitalService,
            HospitalDepartmentRepository hospitalDepartmentRepository) {
        this.appointmentHospitalDeptReservationLogRepository = appointmentHospitalDeptReservationLogRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.reservationProperties = reservationProperties;
        this.hospitalService = hospitalService;
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
    }

    /*   VALIDATE REQUEST INFO :
    *   A. VALIDATE IF REQUESTED DATE AND TIME IS BEFORE CURRENT DATE AND TIME.
    *   B. VALIDATE IF ANY OTHER APPOINTMENT EXISTS ON THE SAME CRITERIA
    *   C. VALIDATE IF REQUESTED APPOINTMENT TIME LIES BETWEEN DOCTOR DUTY ROSTER TIME SCHEDULES
    *
    *   SAVE IN APPOINTMENT RESERVATION LOG ONLY IF IT HAS NOT BEEN SAVED BEFORE
    *   (SAME DOCTOR, SPECIALIZATION, HOSPITAL, APPOINTMENT DATE/TIME */
    @Override
    public Long saveAppointmentHospitalDeptReservationLog(AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG);

        validateRequestedAppointmentInfo(requestDTO);

        Long appointmentReservationLogId = fetchAppointmentReservationLogId(requestDTO);

        if (Objects.isNull(appointmentReservationLogId))
            appointmentReservationLogId = save(requestDTO);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));

        return appointmentReservationLogId;
    }

    /*SCHEDULER - 2 MINS
    * DELETE - 5 MINS*/
    @Override
    public void deleteExpiredAppointmentReservation() {

//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_RESERVATION_LOG);
//
//        List<AppointmentReservationLog> appointmentReservations =
//                appointmentReservationLogRepository.fetchAppointmentReservationLog();
//
//        appointmentReservations.forEach(appointmentReservation -> {
//
//            long expiryDate = appointmentReservation.getCreatedDate().getTime() +
//                    TimeUnit.MINUTES.toMillis(Long.parseLong(reservationProperties.getDeleteIntervalInMinutes()));
//
//            long currentDateInMillis = new Date().getTime();
//
//            if (expiryDate < currentDateInMillis)
//                appointmentReservationLogRepository.delete(appointmentReservation);
//        });
//
//        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));
    }

    private Long save(AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {

        Hospital hospital = hospitalService.fetchActiveHospital(requestDTO.getHospitalId());

//        HospitalDepartment hospitalDepartment = fetchHospitalDepartment(
//                requestDTO.getHospitalDepartmentId(),
//                requestDTO.getHospitalId()
//        );
//
//
//
//        AppointmentHospitalDepartmentReservationLog appointmentReservationLog =
//                parseToAppointmentHospitalDepartmentReservation(requestDTO, hospital);

//        appointmentHospitalDeptReservationLogRepository.save(appointmentReservationLog);
//
//        return appointmentReservationLog.getId();

        return null;
    }

    private Long fetchAppointmentReservationLogId(AppointmentHospitalDeptFollowUpRequestDTO requestDTO) {
        return appointmentHospitalDeptReservationLogRepository.fetchAppointmentHospitalDeptReservationLogId(requestDTO);
    }

    private void validateRequestedAppointmentInfo(AppointmentHospitalDeptFollowUpRequestDTO appointmentInfo) {

//        validateIfRequestIsBeforeCurrentDateTime(
//                appointmentInfo.getAppointmentDate(), appointmentInfo.getAppointmentTime());
//
//        validateIfParentAppointmentExists(appointmentInfo);
//
//        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(appointmentInfo);
//
//        boolean isTimeValid = validateIfRequestedAppointmentTimeIsValid(
//                doctorDutyRosterInfo, appointmentInfo.getAppointmentTime());
//
//        if (!isTimeValid) {
//            log.error(INVALID_APPOINTMENT_TIME, convert24HourTo12HourFormat(appointmentInfo.getAppointmentTime()));
//            throw new NoContentFoundException(String.format(INVALID_APPOINTMENT_TIME,
//                    convert24HourTo12HourFormat(appointmentInfo.getAppointmentTime())));
//        }
    }

    /*VALIDATE IF APPOINTMENT ALREADY EXISTS ON SELECTED DATE AND TIME */
    private void validateIfParentAppointmentExists(AppointmentFollowUpRequestDTO appointmentInfo) {

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getAppointmentTime(),
                appointmentInfo.getDoctorId(),
                appointmentInfo.getSpecializationId()
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

    /*FETCH DOCTOR DUTY ROSTER FOR SELECTED DATE, DOCTOR AND SPECIALIZATION
 * IF DOCTOR DAY OFF = 'Y', THEN DOCTOR IS NOT AVAILABLE AND CANNOT TAKE AN APPOINTMENT*/
    private DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterInfo(AppointmentFollowUpRequestDTO appointmentInfo) {

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getDoctorId(),
                appointmentInfo.getSpecializationId()
        );

        if (doctorDutyRosterInfo.getDayOffStatus().equals(YES)) {
            log.error(DOCTOR_NOT_AVAILABLE, utilDateToSqlDate(appointmentInfo.getAppointmentDate()));
            throw new NoContentFoundException(
                    String.format(DOCTOR_NOT_AVAILABLE, utilDateToSqlDate(appointmentInfo.getAppointmentDate())));
        }

        return doctorDutyRosterInfo;
    }

    /*FETCH DOCTOR DUTY ROSTER INFO FOR SELECTED DATE*/
    private DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterInfo(Date date,
                                                                      Long doctorId,
                                                                      Long specializationId) {

        DoctorDutyRosterTimeResponseDTO overrideRosters =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideTime(date, doctorId, specializationId);

        if (Objects.isNull(overrideRosters))
            return doctorDutyRosterRepository.fetchDoctorDutyRosterTime(date, doctorId, specializationId);

        return overrideRosters;
    }

    private HospitalDepartment fetchHospitalDepartment(Long hospitalDepartmentId, Long hospitalId) {
        return hospitalDepartmentRepository.fetchActiveByIdAndHospitalId(hospitalDepartmentId, hospitalId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));

    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT, id);
        throw new NoContentFoundException(HospitalDepartment.class, "id", id.toString());
    };

}
