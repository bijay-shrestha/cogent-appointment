package com.cogent.cogentappointment.client.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
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

    private String companyCode;

    private String password;

    private String apiKey;

    private String apiSecret;

}
