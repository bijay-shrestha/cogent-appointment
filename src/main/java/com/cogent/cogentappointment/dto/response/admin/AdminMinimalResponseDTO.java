package com.cogent.cogentappointment.dto.response.admin;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author smriti on 2019-08-26
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMinimalResponseDTO implements Serializable {

    private BigInteger id;

    private String fullName;

    private String username;

    private String mobileNumber;

    private String email;

    private Character status;

    private String profileName;

    private String adminCategoryName;

    private Character hasMacBinding;

    private String fileUri;

    private Integer totalItems;
}
