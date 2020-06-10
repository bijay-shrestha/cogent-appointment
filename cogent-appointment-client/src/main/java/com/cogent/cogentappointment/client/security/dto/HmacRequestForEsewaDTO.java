package com.cogent.cogentappointment.client.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/3/20
 */
@Getter
@Setter
@Builder
public class HmacRequestForEsewaDTO implements Serializable {

    private String esewaId;

    private String merchantCode;
}
