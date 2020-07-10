package com.cogent.cogentappointment.commons.dto.request.jasper.reshsceduleLog;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/07/09-2:36 PM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleLogJasperData implements Serializable {

    private String appointmentNumber;
    private String appointmentDateTime;
    private String rescheduleDate;
    private String patientDetails;
    private String esewaId;
    private String registrationNumber;
    private String doctorDetails;


}
