package com.cogent.cogentappointment.admin.dto.request.qualification;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
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
public class QualificationUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

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

    @NotNull
    @NotEmpty
    private String remarks;
}
