package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 08/05/20
 */
@Getter
@Setter
public class DDRRequestDTO implements Serializable {

    @Valid
    private DDRDetailRequestDTO ddrDetail;

    @NotEmpty
    @Size(min = 1)
    @Valid
    private List<DDRShiftRequestDTO> shiftDetail;
}
