package com.cogent.cogentappointment.admin.dto.request.companyProfile;

import com.cogent.cogentappointment.admin.dto.request.profile.ProfileMenuUpdateRequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 2019-09-11
 */
@Getter
@Setter
public class CompanyProfileUpdateRequestDTO implements Serializable {

    @NotNull
    private CompanyProfileUpdateDTO profileDTO;

    @NotEmpty
    private List<ProfileMenuUpdateRequestDTO> profileMenuRequestDTO;
}
