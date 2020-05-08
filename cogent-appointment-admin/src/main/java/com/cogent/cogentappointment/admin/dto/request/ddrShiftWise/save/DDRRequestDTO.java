package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 08/05/20
 */
@Getter
@Setter
public class DDRRequestDTO implements Serializable {

    private DDRDetailRequestDTO ddrDetail;

    private List<DDRShiftRequestDTO> shiftDetail;
}
