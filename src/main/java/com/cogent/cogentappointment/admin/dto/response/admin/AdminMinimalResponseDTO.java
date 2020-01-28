package com.cogent.cogentappointment.admin.dto.response.admin;

import com.cogent.cogentappointment.admin.enums.Gender;
import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-08-26
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMinimalResponseDTO implements Serializable {

    private Long id;

    private String fullName;

    private String username;

    private String email;

    private String mobileNumber;

    private Character status;

    private Character hasMacBinding;

    private Gender gender;

    private String profileName;

    private String hospitalName;

    private String fileUri;

    private Integer totalItems;
}
