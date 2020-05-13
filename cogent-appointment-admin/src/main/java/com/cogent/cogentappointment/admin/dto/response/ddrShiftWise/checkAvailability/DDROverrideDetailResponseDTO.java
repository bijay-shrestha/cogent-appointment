package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRBreakRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
public class DDROverrideDetailResponseDTO implements Serializable {

    private Date date;

    private Long shiftId;

    private String shiftName;

    private String startTime;

    private String endTime;

    private Integer rosterGapDuration;

    private Character offStatus;

    private String remarks;

    private List<DDRBreakRequestDTO> breakDetail;
}
