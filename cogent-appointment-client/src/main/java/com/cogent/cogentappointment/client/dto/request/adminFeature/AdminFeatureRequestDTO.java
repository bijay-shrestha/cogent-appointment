package com.cogent.cogentappointment.client.dto.request.adminFeature;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
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
