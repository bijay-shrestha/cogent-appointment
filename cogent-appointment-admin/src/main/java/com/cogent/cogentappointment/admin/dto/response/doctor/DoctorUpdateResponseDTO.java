package com.cogent.cogentappointment.admin.dto.response.doctor;

import lombok.*;

import java.io.Serializable;
import java.util.List;
/**
 * @author smriti on 14/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateResponseDTO implements Serializable {

    private Long doctorId;

    private String doctorName;

    private String mobileNumber;

    private String code;

    private String email;

    private String gender;

    private String nmcNumber;

    private Character status;

    private String hospitalId;

    private String hospitalName;

    private String remarks;

    private Double appointmentCharge;

    private Double appointmentFollowUpCharge;

    private String fileUri;

    private List<DoctorSalutationResponseDTO> doctorSalutationResponseDTOS;

    private List<DoctorSpecializationResponseDTO> doctorSpecializationResponseDTOS;

    private List<DoctorQualificationResponseDTO> doctorQualificationResponseDTOS;
}
