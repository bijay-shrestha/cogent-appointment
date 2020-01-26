package com.cogent.cogentappointment.dto.request.profile;

import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 7/8/19
 */
@Getter
@Setter
public class ProfileMenuRequestDTO implements Serializable {

    @NotNull
    private Long parentId;

    @NotNull
    private Long userMenuId;

    @NotNull
    private Long roleId;

    @NotNull
    @Status
    private Character status;
}
