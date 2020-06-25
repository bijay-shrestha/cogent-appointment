package com.cogent.cogentappointment.admin.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 25/06/20
 */
@Getter
@Setter
public class AdminAvatarUpdateRequestDTO implements Serializable {

    @NotNull
    private Long adminId;

    private String avatar;
}
