package com.cogent.cogentappointment.admin.dto.response.commons;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author rupak ON 21/04/2020
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditableResponseDTO {

    private String createdBy;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY hh:MM a",timezone = "Asia/Kathmandu")
    private Date createdDate;

    private String lastModifiedBy;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY hh:MM a",timezone = "Asia/Kathmandu+05:45")
    private Date lastModifiedDate;
}
