package com.cogent.cogentappointment.dto.request.hospital;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
public class HospitalSearchRequestDTO implements Serializable {

    private String name;

    private String code;

    private Character status;
}
