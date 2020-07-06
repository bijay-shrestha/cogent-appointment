package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.PKITestDTO;
import com.cogent.cogentappointment.esewa.pki.utils.JacksonUtil;
import com.cogent.cogentappointment.esewa.pki.wrapper.DataWrapper;
import com.cogent.cogentappointment.esewa.service.PKIAuthenticationInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;

/**
 * @author smriti on 06/07/20
 */
@RestController
@RequestMapping(API_V1 + "/pki")
@Api(value = "This is PKI Test Resource")
public class PKITestResource {

    private final PKIAuthenticationInfoService pkiAuthenticationInfoService;

    private final DataWrapper dataWrapper;

    public PKITestResource(PKIAuthenticationInfoService pkiAuthenticationInfoService, DataWrapper dataWrapper) {
        this.pkiAuthenticationInfoService = pkiAuthenticationInfoService;
        this.dataWrapper = dataWrapper;
    }

    @PostMapping("/test")
    @ApiOperation("test")
    public String testPKI() throws IOException, ClassNotFoundException {
        PKITestDTO test = JacksonUtil.get(dataWrapper.getData(), PKITestDTO.class);
        return "tested";
    }

}
