package com.cogent.cogentappointment.admin.dto.response.profile;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
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
public class ProfileDetailResponseDTO extends AuditableResponseDTO implements Serializable {

    private ProfileResponseDTO profileResponseDTO;

    private Map<Long, List<ProfileMenuResponseDTO>> profileMenuResponseDTOS;
}
