package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.availableDate;

import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author smriti on 11/06/20
 */
@Getter
@Setter
public class AppointmentAvailableDateResponseDTO extends StatusResponseDTO implements Serializable {

    private List<LocalDate> availableDates;
}
