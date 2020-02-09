package com.cogent.cogentappointment.client.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 08/02/2020
 */
@Getter
@Setter
public class PatientMinSearchRequestDTO implements Serializable {
    private String esewaId;

    private Character isSelf;

    private Long hospitalId;
}
