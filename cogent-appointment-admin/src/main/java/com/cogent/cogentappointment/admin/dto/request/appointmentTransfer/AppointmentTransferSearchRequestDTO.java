package com.cogent.cogentappointment.admin.dto.request.appointmentTransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/11/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTransferSearchRequestDTO implements Serializable {

    private Date appointmentToDate,appointmentFromDate;

    private String appointmentNumber;

    private Long patientMetaInfoId,specializationId,doctorId;

    private Long hospitalId;
}
