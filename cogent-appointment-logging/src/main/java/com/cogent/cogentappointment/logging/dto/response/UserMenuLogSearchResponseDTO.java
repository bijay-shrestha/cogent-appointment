package com.cogent.cogentappointment.logging.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMenuLogSearchResponseDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY",timezone = "Asia/Kathmandu")
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
