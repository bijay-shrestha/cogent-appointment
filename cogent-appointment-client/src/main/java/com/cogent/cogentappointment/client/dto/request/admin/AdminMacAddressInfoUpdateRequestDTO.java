package com.cogent.cogentappointment.client.dto.request.admin;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti on 2019-09-01
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMacAddressInfoUpdateRequestDTO implements Serializable {

    private Long id;

    private String macAddress;

    private Character status;
}
