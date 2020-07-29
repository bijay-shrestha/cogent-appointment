package com.cogent.cogentappointment.admin.dto.response.hospital;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 27/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalAppointmentServiceTypeResponseDTO implements Serializable {

    private Long hospitalAppointmentServiceTypeId;

    private Long appointmentServiceTypeId;

    private String appointmentServiceTypeName;

    private Character isPrimary;
}
