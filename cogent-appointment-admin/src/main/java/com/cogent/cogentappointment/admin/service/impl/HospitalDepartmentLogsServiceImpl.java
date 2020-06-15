package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentTransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.HospitalDepartmentAppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentHospitalDeptPendingApproval.AppointmentHospitalDepartmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.HospitalDepartmentAppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.HospitalDepartmentTransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.HospitalDepartmentAppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.service.HospitalDepartmentLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SEARCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SEARCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Sauravi Thapa ON 6/12/20
 */

@Slf4j
@Service
@Transactional
public class HospitalDepartmentLogsServiceImpl implements HospitalDepartmentLogsService {

    private final AppointmentRepository appointmentRepository;

    public HospitalDepartmentLogsServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
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
    public List<AppointmentHospitalDepartmentPendingApprovalResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentPendingApprovalSearchDTO searchDTO,
            Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPOINTMENT_APPROVAL);

        List<AppointmentHospitalDepartmentPendingApprovalResponseDTO> pendingAppointments =
                appointmentRepository.searchPendingHospitalDeptAppointments(searchDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PENDING_APPOINTMENT_APPROVAL,
                getDifferenceBetweenTwoTime(startTime));

        return pendingAppointments;
    }
}
