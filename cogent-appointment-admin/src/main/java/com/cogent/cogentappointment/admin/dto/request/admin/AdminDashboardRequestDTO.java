package com.cogent.cogentappointment.admin.dto.request.admin;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
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
public class AdminDashboardRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @Status
    private Character status;
}
