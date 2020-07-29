package com.cogent.cogentappointment.commons.dto.request.jasper.transferLog;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/07/12-7:34 AM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTransferLogJasperData implements Serializable {

    private String appointmentStatus;

    private String appointmentNumber;

    private String patientDetails;

    private String transferFromDateTime;

    private String transferToDateTime;

    private String transferFromDoctor;

    private String transferToDoctor;

    private String doctorDetails;


}
