package com.cogent.cogentappointment.esewa.dto.request.login;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/२
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyDetail implements Serializable {
    private String companyCode;

    private String apiSecret;

    private String apiKey;
}
