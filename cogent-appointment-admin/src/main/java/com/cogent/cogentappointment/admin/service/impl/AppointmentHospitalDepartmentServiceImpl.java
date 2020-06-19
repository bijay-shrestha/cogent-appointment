package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentTransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.HospitalDepartmentAppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.HospitalDepartmentAppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.HospitalDepartmentTransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.HospitalDepartmentAppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.repository.AppointmentHospitalDepartmentFollowUpLogRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentHospitalDepartmentInfoRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.commons.exception.NoContentFoundException;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpLog;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Sauravi Thapa ON 6/12/20
 */

@Slf4j
@Service
@Transactional
public class AppointmentHospitalDepartmentServiceImpl implements AppointmentHospitalDepartmentService {

    private final AppointmentRepository appointmentRepository;

    private final IntegrationCheckPointService integrationCheckPointService;

    private final AppointmentHospitalDepartmentFollowUpLogRepository appointmentHospitalDepartmentFollowUpLogRepository;

    private final AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDepartmentFollowUpTrackerService;

    private final AppointmentHospitalDepartmentFollowUpRequestLogService appointmentHospitalDepartmentFollowUpRequestLogService;

    private final AppointmentHospitalDepartmentInfoRepository appointmentHospitalDepartmentInfoRepository;

    private final PatientService patientService;

    public AppointmentHospitalDepartmentServiceImpl(
            AppointmentRepository appointmentRepository,
            IntegrationCheckPointService integrationCheckPointService,
            AppointmentHospitalDepartmentFollowUpLogRepository appointmentHospitalDepartmentFollowUpLogRepository,
            AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDepartmentFollowUpTrackerService,
            AppointmentHospitalDepartmentFollowUpRequestLogService appointmentHospitalDepartmentFollowUpRequestLogService,
            AppointmentHospitalDepartmentInfoRepository appointmentHospitalDepartmentInfoRepository,
            PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.integrationCheckPointService = integrationCheckPointService;
        this.appointmentHospitalDepartmentFollowUpLogRepository = appointmentHospitalDepartmentFollowUpLogRepository;
        this.appointmentHospitalDepartmentFollowUpTrackerService = appointmentHospitalDepartmentFollowUpTrackerService;
        this.appointmentHospitalDepartmentFollowUpRequestLogService = appointmentHospitalDepartmentFollowUpRequestLogService;
        this.appointmentHospitalDepartmentInfoRepository = appointmentHospitalDepartmentInfoRepository;
        this.patientService = patientService;
    }

    @Override
    public HospitalDepartmentAppointmentLogResponseDTO searchAppointmentLogs(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO,
            Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_APPOINTMENT_LOG);

        HospitalDepartmentAppointmentLogResponseDTO responseDTOS =
                appointmentRepository.searchHospitalDepartmentAppointmentLogs(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_APPOINTMENT_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public HospitalDepartmentTransactionLogResponseDTO searchTransactionLogs(
            HospitalDepartmentTransactionLogSearchDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_TRANSACTION_LOG);

        HospitalDepartmentTransactionLogResponseDTO responseDTOS =
                appointmentRepository.searchHospitalDepartmentTransactionLogs(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_TRANSACTION_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public HospitalDepartmentAppointmentRescheduleLogResponseDTO searchRescheduleLogs(
            HospitalDepartmentAppointmentRescheduleLogSearchDTO rescheduleDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_APPOINTMENT_RESCHEDULE_LOG);

        HospitalDepartmentAppointmentRescheduleLogResponseDTO responseDTOS =
                appointmentRepository.searchHospitalDepartmentRescheduleLogs(rescheduleDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_APPOINTMENT_RESCHEDULE_LOG,
                getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<AppointmentHospitalDepartmentCheckInResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentCheckInSearchDTO searchDTO,
            Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPOINTMENT_APPROVAL);

        List<AppointmentHospitalDepartmentCheckInResponseDTO> pendingAppointments =
                appointmentRepository.searchPendingHospitalDeptAppointments(searchDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PENDING_APPOINTMENT_APPROVAL,
                getDifferenceBetweenTwoTime(startTime));

        return pendingAppointments;
    }

    @Override
    public AppointmentHospitalDepartmentCheckInDetailResponseDTO fetchPendingHospitalDeptAppointmentDetail(
            Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, PENDING_APPOINTMENT_APPROVAL);

        AppointmentHospitalDepartmentCheckInDetailResponseDTO appointmentDetails =
                appointmentRepository.fetchPendingHospitalDeptAppointmentDetail(appointmentId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, PENDING_APPOINTMENT_APPROVAL,
                getDifferenceBetweenTwoTime(startTime));

        return appointmentDetails;
    }

    @Override
    public void approveAppointment(IntegrationBackendRequestDTO integrationRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT);

        // isPatientNew-->      true--> no hospital number | new registration patient
        // isPatientNew-->      false--> hospital number   | registered patient

        Appointment appointment = fetchPendingAppointment(
                integrationRequestDTO.getAppointmentId(), integrationRequestDTO.getHospitalId());

        if (!Objects.isNull(integrationRequestDTO.getIntegrationChannelCode()))
            integrationCheckPointService.apiIntegrationCheckpointForDepartmentAppointment(appointment, integrationRequestDTO);

        appointment.setStatus(APPROVED);

        saveAppointmentHospitalDepartmentFollowUpTracker(appointment);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    private Appointment fetchPendingAppointment(Long appointmentId, Long hospitalId) {
        return appointmentRepository.fetchPendingAppointmentByIdAndHospitalId(appointmentId, hospitalId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException(Appointment.class, "appointmentId", appointmentId.toString());
    };

    /*IF IS FOLLOW UP = 'N',
    *   THEN PERSIST IN AppointmentHospitalDepartmentFollowUpTracker WHERE
    *   ALLOWED NUMBER OF FOLLOW UPS & INTERVAL DAYS IS BASED ON THE SELECTED HOSPITAL.
    *   PERSIST IN APPOINTMENT FOLLOW UP REQUEST WITH REQUEST COUNT STARTING WITH 0.
    *   REGISTER PATIENT AND GENERATE A UNIQUE REGISTRATION NUMBER.
    *
    * ELSE
    *   UPDATE AppointmentHospitalDepartmentFollowUpTracker
    *   ie. DECREMENT NUMBER OF FOLLOW UPS BY 1 AND IF IT IS ZERO, THEN SET STATUS AS 'N'
    *   ONLY ACTIVE ('Y') AppointmentHospitalDepartmentFollowUpTracker ARE FETCHED TO DIFFERENTIATE
     *  WHETHER IT IS FOLLOW UP OR NORMAL APPOINTMENT
    *   */
    private void saveAppointmentHospitalDepartmentFollowUpTracker(Appointment appointment) {

        if (appointment.getIsFollowUp().equals(YES)) {

            AppointmentHospitalDepartmentFollowUpLog appointmentFollowUpLog =
                    appointmentHospitalDepartmentFollowUpLogRepository.findByFollowUpAppointmentId(appointment.getId())
                            .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointment.getId()));

            appointmentHospitalDepartmentFollowUpTrackerService.updateFollowUpTracker(
                    appointmentFollowUpLog.getParentAppointmentId());

        } else {

            AppointmentHospitalDepartmentInfo appointmentHospitalDepartmentInfo =
                    fetchAppointmentHospitalDepartmentInfo(appointment.getId());

            AppointmentHospitalDepartmentFollowUpTracker appointmentFollowUpTracker =
                    appointmentHospitalDepartmentFollowUpTrackerService.save(
                            appointment.getId(),
                            appointment.getHospitalId(),
                            appointmentHospitalDepartmentInfo.getHospitalDepartment(),
                            appointment.getPatientId()
                    );

            appointmentHospitalDepartmentFollowUpRequestLogService.save(appointmentFollowUpTracker);

            registerPatient(appointment.getPatientId().getId(), appointment.getHospitalId().getId());
        }
    }

    private void registerPatient(Long patientId, Long hospitalId) {
        patientService.registerPatient(patientId, hospitalId);
    }

    private AppointmentHospitalDepartmentInfo fetchAppointmentHospitalDepartmentInfo(Long appointmentId) {
        return appointmentHospitalDepartmentInfoRepository.fetchAppointmentHospitalDepartmentInfo(appointmentId)
                .orElseThrow(() -> APPOINTMENT_HOSPITAL_DEPARTMENT_INFO_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_HOSPITAL_DEPARTMENT_INFO_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_HOSPITAL_DEPARTMENT_INFO);
        throw new NoContentFoundException(AppointmentHospitalDepartmentInfo.class,
                "appointmentId", appointmentId.toString());
    };

}
