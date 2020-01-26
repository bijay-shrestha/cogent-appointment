package com.cogent.cogentappointment.dto.request.profile;

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
public class ProfileUpdateRequestDTO implements Serializable {

    @NotNull
    private ProfileUpdateDTO profileDTO;

    @NotEmpty
    private List<ProfileMenuUpdateRequestDTO> profileMenuRequestDTO;
}
