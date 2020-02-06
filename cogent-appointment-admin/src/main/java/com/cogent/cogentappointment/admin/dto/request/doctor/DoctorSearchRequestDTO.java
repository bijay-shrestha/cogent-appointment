package com.cogent.cogentappointment.admin.dto.request.doctor;

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

    private Long id;

    private String code;

    private Character status;

    private String mobileNumber;

    private Long specializationId;

    private Long hospitalId;
}
