package com.cogent.cogentappointment.admin.dto.request.hospitalDepartment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
public class HospitalDepartmentSearchRequestDTO implements Serializable {

    private Long hospitalId;

    private Long id;

    private String code;

    private Long doctorId;

    private Long roomId;

    private Character status;
}
