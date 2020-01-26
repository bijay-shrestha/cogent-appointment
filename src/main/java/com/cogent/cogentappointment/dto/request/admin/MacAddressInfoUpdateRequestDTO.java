package com.cogent.cogentappointment.dto.request.admin;

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
public class MacAddressInfoUpdateRequestDTO implements Serializable {

    private Long id;

    private String macAddress;

    private Character status;
}
