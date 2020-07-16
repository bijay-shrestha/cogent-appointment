package com.cogent.cogentthirdpartyconnector.response.integrationThirdParty;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/11-3:44 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyResponse implements Serializable {

    private String status;

    private String code;

    private String message;
}
