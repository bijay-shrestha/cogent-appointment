package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 18/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRTimeResponseDTO implements Serializable {

    private Date startTime;

    private Date endTime;

    private String shiftName;
}
