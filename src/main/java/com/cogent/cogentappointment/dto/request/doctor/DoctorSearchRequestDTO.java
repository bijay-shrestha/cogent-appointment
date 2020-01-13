package com.cogent.cogentappointment.dto.request.doctor;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-27
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSearchRequestDTO implements Serializable {

    private String name;

    private String code;

    private Character status;

    private String mobileNumber;

    private Long specializationId;
}
