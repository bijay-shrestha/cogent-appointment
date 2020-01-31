package com.cogent.cogentappointment.admin.security.hmac;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Component
@Getter
public class HMACConfig {

    @Value("${hmac.uri:/api/v1/**}")
    private String uri;

    @Value("${hmac.header:Authorization}")
    private String header;
}
