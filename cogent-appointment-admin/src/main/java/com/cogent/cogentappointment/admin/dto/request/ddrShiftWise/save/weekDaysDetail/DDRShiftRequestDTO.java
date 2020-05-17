package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 08/05/20
 */
@Getter
@Setter
public class DDRShiftRequestDTO implements Serializable {

    @NotNull
    private Long shiftId;

    @NotNull
    private Integer rosterGapDuration;

    @NotEmpty
    @Size(min = 1, max = 7)
    private List<DDRWeekDaysDetailRequestDTO> weekDaysDetail;
}
