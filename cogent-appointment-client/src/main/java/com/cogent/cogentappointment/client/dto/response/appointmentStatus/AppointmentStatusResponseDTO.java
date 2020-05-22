package com.cogent.cogentappointment.client.dto.response.appointmentStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author smriti ON 16/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusResponseDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private LocalDate date;

    private Long doctorId;

    private Long specializationId;

    private String appointmentTimeDetails;

    private String appointmentNumber;

    private String mobileNumber;

    private String age;

    private String gender;

    private String patientName;

    private Long appointmentId;

    private Character isFollowUp;

    private Character hasTransferred;
}
