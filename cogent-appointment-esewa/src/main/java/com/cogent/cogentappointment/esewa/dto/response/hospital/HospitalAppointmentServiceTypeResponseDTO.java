package com.cogent.cogentappointment.esewa.dto.response.hospital;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 14/06/20
 */
@Getter
@Setter
public class HospitalAppointmentServiceTypeResponseDTO implements Serializable {

    private Long appointmentServiceTypeId;

    private String appointmentServiceTypeName;

    private Character isPrimary;
}
