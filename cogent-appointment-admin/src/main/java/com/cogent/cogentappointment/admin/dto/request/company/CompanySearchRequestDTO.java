package com.cogent.cogentappointment.admin.dto.request.company;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
public class CompanySearchRequestDTO implements Serializable {

    private String name;

    private String hospitalCode;

    private Character status;
}
