package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.dashboard.RefundAmountRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.HospitalDepartmentAppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.HospitalDepartmentRefundStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRefundDetailRepositoryCustom")
public interface AppointmentRefundDetailRepositoryCustom {

    Double getTotalRefundedAmount(RefundAmountRequestDTO refundAmountRequestDTO);

    RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO, Pageable pageable);

    HospitalDepartmentRefundStatusResponseDTO searchHospitalDepartmentRefundAppointments
            (RefundStatusSearchRequestDTO requestDTO, Pageable pageable);

    AppointmentRefundDetail fetchAppointmentRefundDetail(RefundStatusRequestDTO requestDTO);

    AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId);

    HospitalDepartmentAppointmentRefundDetailResponseDTO fetchHospitalDepartmentRefundDetailsById(Long appointmentId);
}
