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

    private Long companyId;

    private String companyCode;

    private Character status;
}
