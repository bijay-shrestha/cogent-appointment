package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.admin.service.impl.AdminServiceImpl;
import com.cogent.cogentappointment.admin.service.impl.AuthenticateServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AuthenticateConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AuthenticateConstant.LOGIN_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.LOGIN;

/**
 * @author Sauravi Thapa २०/१/१३
 */

@RestController
@CrossOrigin
@RequestMapping(API_V1)
@Api(BASE_API_VALUE)
public class LoginResource {

    private final AuthenticateServiceImpl authenticateService;

    private final AdminServiceImpl adminService;

    public LoginResource(AuthenticateServiceImpl authenticateService, AdminServiceImpl adminService) {
        this.authenticateService = authenticateService;
        this.adminService = adminService;
    }

    @PostMapping(LOGIN)
    @ApiOperation(LOGIN_OPERATION)
    public ResponseEntity<AdminLoggedInInfoResponseDTO> login(HttpServletRequest request,
                                                              @RequestBody LoginRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authenticateService.loginUser(request, requestDTO));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
