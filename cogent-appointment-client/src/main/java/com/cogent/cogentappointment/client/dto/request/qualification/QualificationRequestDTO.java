package com.cogent.cogentappointment.client.dto.request.qualification;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificationRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Long universityId;


    @NotNull
    private Long qualificationAliasId;

    @NotNull
    @Status
    private Character status;
}
