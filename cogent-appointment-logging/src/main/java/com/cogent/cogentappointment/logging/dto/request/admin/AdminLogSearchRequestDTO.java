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
public class AdminLogSearchRequestDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long hospitalId;

    private Long parentId;
    
    private Long roleId;


}
