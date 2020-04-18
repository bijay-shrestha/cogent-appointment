package com.cogent.cogentappointment.admin.dto.request.adminFeature;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 18/04/20
 */
@Getter
@Setter
public class AdminFeatureRequestDTO implements Serializable {

    @NotNull
    @Status
    private Character isSideBarCollapse;
}
