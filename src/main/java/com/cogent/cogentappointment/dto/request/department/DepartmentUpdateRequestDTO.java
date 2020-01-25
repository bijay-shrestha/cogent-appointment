package com.cogent.cogentappointment.dto.request.department;

import com.cogent.cogentappointment.constraintvalidator.Status;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class DepartmentUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private String remarks;
}
