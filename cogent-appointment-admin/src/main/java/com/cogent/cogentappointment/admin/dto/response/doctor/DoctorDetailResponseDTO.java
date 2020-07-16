package com.cogent.cogentappointment.admin.dto.response.doctor;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

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

    private String doctorSalutation;

    private String mobileNumber;

    private String code;

    private Character status;

    private String email;

    private String nmcNumber;

    private String gender;

    private String specializationName;

    private String qualificationName;

    private String hospitalName;

    private String remarks;

    private Double appointmentCharge;

    private Double appointmentFollowUpCharge;

    private String fileUri;
}
