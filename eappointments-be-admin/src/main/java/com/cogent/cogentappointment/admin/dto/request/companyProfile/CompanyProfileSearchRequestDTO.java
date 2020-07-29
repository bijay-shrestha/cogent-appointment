package com.cogent.cogentappointment.admin.dto.request.companyProfile;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-11
 */
@Getter
@Setter
public class CompanyProfileSearchRequestDTO implements Serializable {

    private Long companyProfileId;

    private Character status;

    private Long companyId;
}
