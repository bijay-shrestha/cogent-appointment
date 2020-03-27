package com.cogent.cogentappointment.admin.dto.response.admin;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoggedInAdminDTO implements Serializable{
    private Long id;

    private String username;

    private String password;

    private Character isCompany;

    private String companyCode;

    private Long companyId;

    private String apiKey;

    private String apiSecret;
}
