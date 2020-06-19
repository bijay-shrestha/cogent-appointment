package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.RefundStatusResponseDTO;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public interface RefundStatusService {

    RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO, Pageable pageable);

    void checkRefundStatus(RefundStatusRequestDTO requestDTO, IntegrationRefundRequestDTO integrationRefundRequestDTO) throws IOException;

    AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long id);

}
