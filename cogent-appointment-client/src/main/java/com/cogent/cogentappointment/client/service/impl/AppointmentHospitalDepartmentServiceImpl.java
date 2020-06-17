package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentHospitalDepartment.AppointmentHospitalDepartmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.service.AppointmentHospitalDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

//    private final AppointmentHospitalDepartmentFollowUpRequestLogRe

    public AppointmentHospitalDepartmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<AppointmentHospitalDepartmentCheckInResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentPendingApprovalSearchDTO searchDTO,
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


        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    /*IF IS FOLLOW UP = 'N',
    *   THEN PERSIST IN AppointmentFollowUpTracker WHERE
    *   ALLOWED NUMBER OF FOLLOW UPS & INTERVAL DAYS IS BASED ON THE SELECTED HOSPITAL.
    *   PERSIST IN APPOINTMENT FOLLOW UP REQUEST WITH REQUEST COUNT STARTING WITH 0.
    *   REGISTER PATIENT AND GENERATE A UNIQUE REGISTRATION NUMBER.
    *
    * ELSE
    *   UPDATE APPOINTMENT FOLLOW UP TRACKER
    *   ie. DECREMENT NUMBER OF FOLLOW UPS BY 1 AND IF IT IS ZERO, THEN SET STATUS AS 'N'
    *   ONLY ACTIVE ('Y') APPOINTMENT FOLLOW UP TRACKER ARE FETCHED TO DIFFERENTIATE
     *  WHETHER IT IS FOLLOW UP OR NORMAL APPOINTMENT
    *   */
//    private void saveAppointmentFollowUpTracker(Appointment appointment) {
//
//        if (appointment.getIsFollowUp().equals(YES)) {
//
//            AppointmentHospitalDepartmentFollowUpLog appointmentFollowUpLog =
//                    appointmentFollowUpLogRepository.findByFollowUpAppointmentId(appointment.getId())
//                            .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointment.getId()));
//
//            appointmentFollowUpTrackerService.updateFollowUpTracker(appointmentFollowUpLog.getParentAppointmentId());
//
//        } else {
//
//            AppointmentDoctorInfo appointmentDoctorInfo = fetchAppointmentDoctorInfo(appointment.getId());
//
//            AppointmentFollowUpTracker appointmentFollowUpTracker =
//                    appointmentFollowUpTrackerService.save(
//                            appointment.getId(),
//                            appointment.getHospitalId(),
//                            appointmentDoctorInfo.getDoctor(),
//                            appointmentDoctorInfo.getSpecialization(),
//                            appointment.getPatientId()
//                    );
//
//            appointmentFollowUpRequestLogService.save(appointmentFollowUpTracker);
//
//            registerPatient(appointment.getPatientId().getId(), appointment.getHospitalId().getId());
//        }
//    }
//
//    private void registerPatient(Long patientId, Long hospitalId) {
//        patientService.registerPatient(patientId, hospitalId);
//    }

}
