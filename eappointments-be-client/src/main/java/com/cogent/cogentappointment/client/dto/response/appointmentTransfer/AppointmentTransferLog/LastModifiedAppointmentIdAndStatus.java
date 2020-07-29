package com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/12/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LastModifiedAppointmentIdAndStatus implements Serializable {

    private Long appointmentTransferredId;

    private String status;
}
