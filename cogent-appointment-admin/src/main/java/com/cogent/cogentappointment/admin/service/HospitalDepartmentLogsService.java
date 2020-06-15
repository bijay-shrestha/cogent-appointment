package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentTransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.HospitalDepartmentAppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.HospitalDepartmentAppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.HospitalDepartmentTransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.HospitalDepartmentAppointmentRescheduleLogResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Sauravi Thapa ON 6/12/20
 */

public interface HospitalDepartmentLogsService {

    HospitalDepartmentAppointmentLogResponseDTO searchAppointmentLogs(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO, Pageable pageable);

    HospitalDepartmentTransactionLogResponseDTO searchTransactionLogs(
            HospitalDepartmentTransactionLogSearchDTO searchRequestDTO, Pageable pageable);

    HospitalDepartmentAppointmentRescheduleLogResponseDTO searchRescheduleLogs(
            HospitalDepartmentAppointmentRescheduleLogSearchDTO rescheduleDTO, Pageable pageable);

}
