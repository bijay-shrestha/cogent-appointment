package com.cogent.cogentappointment.admin.dto.request.companyProfile;

import com.cogent.cogentappointment.admin.dto.request.profile.ProfileMenuRequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 25/03/20
 */
@Getter
@Setter
public class CompanyProfileRequestDTO implements Serializable {

    private CompanyProfileDTO companyProfileInfo;

    @Valid
    private List<ProfileMenuRequestDTO> profileMenuInfo;
}


