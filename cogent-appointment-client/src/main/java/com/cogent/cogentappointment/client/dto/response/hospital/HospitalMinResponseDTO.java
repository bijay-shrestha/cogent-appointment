package com.cogent.cogentappointment.client.dto.response.hospital;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 29/01/2020
 */
@Getter
@Setter
public class HospitalMinResponseDTO implements Serializable {

    private String name;

    private String address;

    private String fileUri;

    private List<String> contactNumbers;
}
