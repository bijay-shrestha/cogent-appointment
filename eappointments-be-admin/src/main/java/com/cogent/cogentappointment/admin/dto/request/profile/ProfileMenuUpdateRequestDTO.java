package com.cogent.cogentappointment.admin.dto.request.profile;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-11
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileMenuUpdateRequestDTO implements Serializable {

    @NotNull
    private Long profileMenuId;

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
