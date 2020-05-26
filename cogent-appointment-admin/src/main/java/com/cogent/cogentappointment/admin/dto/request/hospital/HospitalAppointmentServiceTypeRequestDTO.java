package com.cogent.cogentappointment.admin.dto.request.hospital;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 26/05/20
 */
@Getter
@Setter
public class HospitalAppointmentServiceTypeRequestDTO implements Serializable {

    private Long appointmentServiceTypeId;

    private Character isPrimary;

    private Character status;
}
