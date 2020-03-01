package com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPatientDetail;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailByAppointmentTimeRequestDTO implements Serializable {

    @NotNull
    private Long hospitalId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    private String appointmentTime;


}
