package com.cogent.cogentappointment.client.dto.response.doctor;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author smriti on 2019-09-30
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDetailResponseDTO extends AuditableResponseDTO implements Serializable {

    private BigInteger id;

    private String doctorName;

    private String mobileNumber;

    private String code;

    private Character status;

    private String email;

    private String nmcNumber;

    private String gender;

    private String specializationName;

    private String qualificationName;

    private String remarks;

    private Double appointmentCharge;

    private Double appointmentFollowUpCharge;

    private String fileUri;
}
