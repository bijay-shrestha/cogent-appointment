package com.cogent.cogentappointment.logging.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date logDate;

    private String logTime;

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
