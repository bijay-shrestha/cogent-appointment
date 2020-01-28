package com.cogent.cogentappointment.client.dto.response.admin;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-08-29
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMacAddressInfoResponseDTO implements Serializable {

    private Long id;

    private String macAddress;
}
