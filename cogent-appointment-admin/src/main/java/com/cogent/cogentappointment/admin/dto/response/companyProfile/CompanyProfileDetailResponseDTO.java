package com.cogent.cogentappointment.admin.dto.response.companyProfile;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileMenuResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author smriti on 7/15/19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfileDetailResponseDTO extends AuditableResponseDTO implements Serializable {

    private CompanyProfileResponseDTO companyProfileInfo;

    private Map<Long, List<ProfileMenuResponseDTO>> companyProfileMenuInfo;
}
