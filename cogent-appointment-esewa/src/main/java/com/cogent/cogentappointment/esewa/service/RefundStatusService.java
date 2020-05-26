package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.refundStatus.RefundStatusRequestDTO;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public interface RefundStatusService {

    void approveAppointmentRefund(RefundStatusRequestDTO requestDTO);

}
