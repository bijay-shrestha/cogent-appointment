package com.cogent.cogentappointment.logging.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
public class AdminClientLogSearchRequestDTO implements Serializable {

    private Long clienId;

    private Date fromDate;

    private Date toDate;

    private Long adminMetaInfoId;

    private Long parentId;

    private Long roleId;
}
