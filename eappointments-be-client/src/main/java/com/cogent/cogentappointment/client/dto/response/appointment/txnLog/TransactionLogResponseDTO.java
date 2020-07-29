package com.cogent.cogentappointment.client.dto.response.appointment.txnLog;

import com.cogent.cogentappointment.client.dto.response.appointment.log.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 4/19/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionLogResponseDTO implements Serializable {

    private List<TransactionLogDTO> transactionLogs;

    private BookedAppointmentResponseDTO bookedInfo;

    private CheckedInAppointmentResponseDTO checkedInInfo;

    private CancelledAppointmentResponseDTO cancelledInfo;

    private RefundAppointmentResponseDTO refundInfo;

    private RevenueFromRefundAppointmentResponseDTO revenueFromRefundInfo;

    private Double totalAmount;

    private int totalItems;
}
