package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.shift;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 19/05/20
 */
@Getter
@Setter
public class DDRShiftUpdateRequestDTO implements Serializable {

    @NotNull
    private Long ddrId;

    private List<DDRShiftUpdateDTO> shiftDetail;
}


