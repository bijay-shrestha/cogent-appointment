package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.PKITestDTO;
import com.cogent.cogentappointment.esewa.pki.wrapper.DataWrapper;
import com.cogent.cogentappointment.esewa.repository.PKIAuthenticationInfoRepository;
import com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.TestConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.TestConstant.TEST_OPERATION;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.TestResourceConstant.BASE_TEST_RESOURCE;

@RestController
@RequestMapping(value = API_V1 + BASE_TEST_RESOURCE)
@Api(BASE_API_VALUE)
public class TestResource {

    private final DataWrapper dataWrapper;

    private final PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository;

    public TestResource(DataWrapper dataWrapper, PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository) {
        this.dataWrapper = dataWrapper;
        this.pkiAuthenticationInfoRepository = pkiAuthenticationInfoRepository;
    }

    @GetMapping
    @ApiOperation(TEST_OPERATION)
    public String testClient() {
        return "Congratulations! e-Appointment esewa module is running successfully in Kubeshpere.. testing! ....";
    }

    @PostMapping("/pki")
    @ApiOperation("test")
    public String testPKI() throws IOException {
//        PKITestDTO test = JacksonUtil.get(dataWrapper.getData(), PKITestDTO.class);
        PKITestDTO test = ObjectMapperUtils.convertValue(dataWrapper.getData(), PKITestDTO.class);
        return "tested";
    }

}
