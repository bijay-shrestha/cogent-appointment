package com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/11/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTransferLogResponseDTO implements Serializable{

   private List<AppointmentTransferLogDTO> response;

   private int totalItems;
}
