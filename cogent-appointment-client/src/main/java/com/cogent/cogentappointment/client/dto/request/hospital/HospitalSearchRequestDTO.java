package com.cogent.cogentappointment.client.dto.request.hospital;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
public class HospitalSearchRequestDTO implements Serializable {

    private String name;

    private String hospitalCode;

    private Character status;
}
