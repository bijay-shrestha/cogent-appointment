package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.availableDate;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author smriti on 23/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentAvailableDateDTO implements Serializable {

    private LocalDate availableDate;

    private String availableDateNepali;
}
