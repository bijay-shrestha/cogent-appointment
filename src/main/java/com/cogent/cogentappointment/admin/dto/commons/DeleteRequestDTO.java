package com.cogent.cogentappointment.admin.dto.commons;

import com.cogent.cogentappointment.admin.constraintvalidator.DeleteStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class DeleteRequestDTO implements Serializable {
    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String remarks;

    @NotNull
    @DeleteStatus
    private Character status;

}
