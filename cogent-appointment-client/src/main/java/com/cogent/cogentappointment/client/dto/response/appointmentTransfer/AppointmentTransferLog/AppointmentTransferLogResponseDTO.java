package com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
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
