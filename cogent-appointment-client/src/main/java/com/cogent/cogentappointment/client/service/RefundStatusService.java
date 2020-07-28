package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.HospitalDepartmentAppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.HospitalDepartmentRefundStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public interface RefundStatusService {

    RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO, Pageable pageable);

    HospitalDepartmentRefundStatusResponseDTO searchHospitalDepartmentRefundAppointments(
            RefundStatusSearchRequestDTO requestDTO, Pageable pageable);

    void checkRefundStatus(RefundStatusRequestDTO requestDTO);

    AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long id);

    HospitalDepartmentAppointmentRefundDetailResponseDTO fetchHospitalDepartmentRefundDetailsById(Long id);

}
