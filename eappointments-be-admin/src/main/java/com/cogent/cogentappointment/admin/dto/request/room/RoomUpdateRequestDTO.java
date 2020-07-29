package com.cogent.cogentappointment.admin.dto.request.room;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-25
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateRequestDTO implements Serializable {
    @NotNull
    private Long id;

    @NotNull
    private String roomNumber;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private Long hospitalId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;
}
