package com.cogent.cogentappointment.admin.dto.request.doctor;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 07/05/20
 */
@Getter
@Setter
public class DoctorShiftRequestDTO implements Serializable {

    private Long doctorId;

    private List<Long> shiftIds;
}
