package com.cogent.cogentappointment.esewa.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/१/३१
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminMinDetails implements Serializable {
    private String username;

    private String fullName;

    private String email;

    private String hospitalCode;

    private Long hospitalId;

    private Character isCompany;

    private String password;

    private String apiKey;

    private String apiSecret;

}
