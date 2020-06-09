package com.cogent.cogentthirdpartyconnector.resource;

import com.cogent.cogentthirdpartyconnector.service.ThirdPartyService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rupak ON 2020/06/09-1:35 PM
 */
@RestController
public class ThirdPartyResource {

    private final ThirdPartyService thirdPartyService;

    public ThirdPartyResource(ThirdPartyService thirdPartyService) {
        this.thirdPartyService = thirdPartyService;
    }
}
