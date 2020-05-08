package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.login.LoginEsewaRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.service.impl.AuthenticateServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AuthenticateConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AuthenticateConstant.LOGIN_OPERATION;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;

/**
 * @author Sauravi Thapa २०/१/१३
 */

@RestController
@CrossOrigin
@RequestMapping(API_V1)
@Api(BASE_API_VALUE)
public class LoginResource {

    private final AuthenticateServiceImpl authenticateService;

    public LoginResource(AuthenticateServiceImpl authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping(LOGIN)
    @ApiOperation(LOGIN_OPERATION)
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authenticateService.loginUser(requestDTO));
        headers.add("email",requestDTO.getEmail());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping(LOGOUT)
    @ApiOperation(LOGIN_OPERATION)
    public ResponseEntity<?> logout() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(LOGIN+"/eSewa")
    @ApiOperation(LOGIN_OPERATION)
    public ResponseEntity<?> logineSewa(@RequestBody LoginEsewaRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authenticateService.loginThirdParty(requestDTO));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }


}
