package com.cogent.cogentappointment.client.dto.request.hospital;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 29/01/2020
 */
@Getter
@Setter
public class HospitalMinSearchRequestDTO implements Serializable {
    private String name;
}
