package com.cogent.cogentappointment.esewa.dto.request.eSewa;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
public class AvailabeDatesRequestDTO implements Serializable {

    private Long doctorId;

    private Long specializationId;

    private Long hospitalId;
}
