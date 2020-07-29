package com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment;

import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 08/06/20
 */
@Getter
@Setter
public class HospitalDepartmentWithStatusResponseDTO extends StatusResponseDTO implements Serializable {

    private List<HospitalDepartmentResponseDTO> departmentInfo;
}
