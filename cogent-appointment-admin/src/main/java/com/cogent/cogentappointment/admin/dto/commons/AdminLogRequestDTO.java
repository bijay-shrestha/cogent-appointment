package com.cogent.cogentappointment.admin.dto.commons;

import com.cogent.cogentappointment.persistence.model.Admin;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
public class AdminLogRequestDTO implements Serializable {

    @NotNull
    private Admin adminId;

    @NotNull
    private Long parentId;

    @NotNull
    private Long roleId;


    @NotNull
    @NotEmpty
    private String logDescription;

    @NotNull
    @NotEmpty
    private String feature;

    @NotNull
    @NotEmpty
    private String actionType;

    @NotNull
    private Character status;
}
