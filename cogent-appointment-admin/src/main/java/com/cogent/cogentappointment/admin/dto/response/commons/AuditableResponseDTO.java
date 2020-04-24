package com.cogent.cogentappointment.admin.dto.response.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;
}
