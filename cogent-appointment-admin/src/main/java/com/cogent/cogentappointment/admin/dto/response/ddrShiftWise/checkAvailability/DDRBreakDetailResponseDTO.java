package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 14/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRBreakDetailResponseDTO implements Serializable{

    private Long breakTypeId;

    private String breakTypeName;

    private Date startTime;

    private Date endTime;

    private String remarks;
}
