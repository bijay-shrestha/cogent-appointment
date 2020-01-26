package com.cogent.cogentappointment.dto.request.profile;

import lombok.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 7/4/19
 */
@Getter
@Setter
public class ProfileRequestDTO implements Serializable {

    @Valid
    private ProfileDTO profileDTO;

    @Valid
    private List<ProfileMenuRequestDTO> profileMenuRequestDTO;
}
