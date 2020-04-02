package com.cogent.cogentappointment.esewa.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/९
 */
@Getter
@Setter
public class AppointmentCountResponseDTO implements Serializable {

    private Long totalAppointment;

    private Long newPatient;

    private Long registeredPatient;

    private Character pillType;
}
