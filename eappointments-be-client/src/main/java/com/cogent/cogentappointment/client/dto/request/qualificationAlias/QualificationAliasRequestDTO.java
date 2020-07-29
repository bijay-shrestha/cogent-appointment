package com.cogent.cogentappointment.client.dto.request.qualificationAlias;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificationAliasRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Status
    private Character status;

}
