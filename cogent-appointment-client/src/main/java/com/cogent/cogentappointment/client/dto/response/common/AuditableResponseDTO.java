package com.cogent.cogentappointment.client.dto.response.common;

import lombok.*;

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
