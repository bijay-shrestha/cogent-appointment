package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentHospitalDepartmentFollowUpLogRepository;
import com.cogent.cogentappointment.client.repository.AppointmentHospitalDepartmentInfoRepository;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.service.*;
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

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author smriti on 16/06/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentServiceImpl implements AppointmentHospitalDepartmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentHospitalDepartmentFollowUpLogRepository appointmentHospitalDepartmentFollowUpLogRepository;

    private final PatientService patientService;

    private final AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDepartmentFollowUpTrackerService;

    private final AppointmentHospitalDepartmentFollowUpRequestLogService appointmentHospitalDepartmentFollowUpRequestLogService;

    private final AppointmentHospitalDepartmentInfoRepository appointmentHospitalDepartmentInfoRepository;

    private final IntegrationCheckPointService integrationCheckPointService;

    public AppointmentHospitalDepartmentServiceImpl(
            AppointmentRepository appointmentRepository,
            AppointmentHospitalDepartmentFollowUpLogRepository appointmentHospitalDepartmentFollowUpLogRepository,
            PatientService patientService,
            AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDepartmentFollowUpTrackerService,
            AppointmentHospitalDepartmentFollowUpRequestLogService appointmentHospitalDepartmentFollowUpRequestLogService,
            AppointmentHospitalDepartmentInfoRepository appointmentHospitalDepartmentInfoRepository,
            IntegrationCheckPointService integrationCheckPointService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentHospitalDepartmentFollowUpLogRepository = appointmentHospitalDepartmentFollowUpLogRepository;
        this.patientService = patientService;
        this.appointmentHospitalDepartmentFollowUpTrackerService = appointmentHospitalDepartmentFollowUpTrackerService;
        this.appointmentHospitalDepartmentFollowUpRequestLogService = appointmentHospitalDepartmentFollowUpRequestLogService;
        this.appointmentHospitalDepartmentInfoRepository = appointmentHospitalDepartmentInfoRepository;
        this.integrationCheckPointService = integrationCheckPointService;
    }

    @Override
    public List<AppointmentHospitalDepartmentCheckInResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentCheckInSearchDTO searchDTO,
            Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPOINTMENTS);

        List<AppointmentHospitalDepartmentCheckInResponseDTO> pendingAppointments =
                appointmentRepository.searchPendingHospitalDeptAppointments(searchDTO, pageable,
                        getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, PENDING_APPOINTMENTS,
                getDifferenceBetweenTwoTime(startTime));

        return pendingAppointments;
    }

    @Override
    public AppointmentHospitalDepartmentCheckInDetailResponseDTO fetchPendingHospitalDeptAppointmentDetail(
            Long appointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, PENDING_APPOINTMENTS);

        AppointmentHospitalDepartmentCheckInDetailResponseDTO appointmentDetails =
                appointmentRepository.fetchPendingHospitalDeptAppointmentDetail(appointmentId, getLoggedInHospitalId());

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, PENDING_APPOINTMENTS,
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
                integrationRequestDTO.getAppointmentId(), getLoggedInHospitalId());

        if (!Objects.isNull(integrationRequestDTO.getIntegrationChannelCode()))
            integrationCheckPointService.apiIntegrationCheckpointDepartmentWise(appointment, integrationRequestDTO);

        appointment.setStatus(APPROVED);

        saveAppointmentHospitalDepartmentFollowUpTracker(appointment);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    private Appointment fetchPendingAppointment(Long appointmentId, Long hospitalId) {
        return appointmentRepository.fetchPendingAppointmentByIdAndHospitalId(appointmentId, hospitalId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

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

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException(Appointment.class, "appointmentId", appointmentId.toString());
    };

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
