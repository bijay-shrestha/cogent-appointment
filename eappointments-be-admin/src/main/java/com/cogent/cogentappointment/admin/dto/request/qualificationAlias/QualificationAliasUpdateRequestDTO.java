package com.cogent.cogentappointment.admin.dto.request.qualificationAlias;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Rupak
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificationAliasUpdateRequestDTO {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;

}
