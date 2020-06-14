package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 10/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDepartmentDoctorInfoResponseDTO implements Serializable {

    private String doctorName;

    private String fileUri;
}
