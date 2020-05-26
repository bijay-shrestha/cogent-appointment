package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public interface RefundStatusService {

    RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO, Pageable pageable);

    void checkRefundStatus(RefundStatusRequestDTO requestDTO);

}
