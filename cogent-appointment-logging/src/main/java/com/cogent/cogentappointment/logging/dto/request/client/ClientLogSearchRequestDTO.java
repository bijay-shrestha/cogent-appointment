package com.cogent.cogentappointment.logging.dto.request.client;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
public class ClientLogSearchRequestDTO implements Serializable {

    private Long clientId;

    private Date fromDate;

    private Date toDate;

    private Long adminMetaInfoId;

    private Long parentId;

    private Long roleId;
}
