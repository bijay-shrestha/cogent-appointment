package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 17/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRShiftDetailResponseDTO implements Serializable {

    private Long ddrShiftDetailId;

    private Long shiftId;

    private String shiftName;

    private Integer rosterGapDuration;
}
