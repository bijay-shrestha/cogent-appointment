package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                         Pageable pageable);

    void approveRefundAppointment(Long appointmentId);

    void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO);

    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);
}
