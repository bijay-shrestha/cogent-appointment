package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRShiftMinResponseDTO implements Serializable{

    private Long shiftId;

    private String shiftName;

    private Integer rosterGapDuration;

    private Character status;
}
