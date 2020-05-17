package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class DDRResponseDTO extends AuditableResponseDTO implements Serializable {

    private Long ddrId;

    private String hospitalName;

    private String specializationName;

    private String doctorName;

    private String fileUri;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY", timezone = "Asia/Kathmandu")
    private Date toDate;

    private Character status;

    private String remarks;

    private Character hasOverride;
}
