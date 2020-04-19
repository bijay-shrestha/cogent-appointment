package com.cogent.cogentappointment.logging.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMenuLogSearchResponseDTO implements Serializable {

    private String logDateTime;

    private String browser;

    private String os;

    private String location;

    private String email;

    private String mobileNumber;

    private String ipAddress;

    private String feature;

    private String actionType;

    private String logDescription;

    private Character status;

}
