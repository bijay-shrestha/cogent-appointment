package com.cogent.cogentappointment.esewa.resource.v1;

import com.cogent.cogentappointment.esewa.dto.request.LoginRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.esewa.repository.HmacApiInfoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.security.hmac.HMACUtils.getAuthToken;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 4/3/20
 */
@RestController
@RequestMapping("/login")
public class LoginResource {

    private final HmacApiInfoRepository hmacApiInfoRepository;

    public LoginResource(HmacApiInfoRepository hmacApiInfoRepository) {
        this.hmacApiInfoRepository = hmacApiInfoRepository;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO requestDTO) {
        ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailsByHospitalCode(requestDTO.getCompanyCode());

        String authToken = getAuthToken(thirdPartyDetail);

        return ok(authToken);
    }
}
