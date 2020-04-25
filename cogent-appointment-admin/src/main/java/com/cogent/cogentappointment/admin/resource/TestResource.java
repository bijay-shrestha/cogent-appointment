package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.utils.commons.NetworkUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;

/**
 * @author smriti on 25/03/20
 */
@RestController
@RequestMapping(API_V1)
@Api("hello")
public class TestResource {

    @GetMapping("/hello")
    public String test() {
        return "hello world";
    }

    @GetMapping("/mac")
    public String getMac(HttpServletRequest request) {
        String mac = NetworkUtils.getDeviceAddress.apply(request);
        return mac;
    }
}
