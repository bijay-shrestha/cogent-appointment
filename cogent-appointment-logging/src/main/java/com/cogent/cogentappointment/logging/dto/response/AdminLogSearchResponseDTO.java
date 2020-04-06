package com.cogent.cogentappointment.logging.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
public class AdminLogSearchResponseDTO implements Serializable {

    private Date logDate;

    private String userName;

    private String ipAddress;

    private String feature;

    private String actionType;

    private String logDescription;

}
