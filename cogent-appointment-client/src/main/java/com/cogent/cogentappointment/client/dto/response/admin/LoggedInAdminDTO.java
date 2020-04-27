package com.cogent.cogentappointment.client.dto.response.admin;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoggedInAdminDTO implements Serializable{
    private Long id;

    private String email;

    private String password;

    private Character isCompany;

    private String hospitalCode;

    private Long hospitalId;

    private String apiKey;

    private String apiSecret;
}
