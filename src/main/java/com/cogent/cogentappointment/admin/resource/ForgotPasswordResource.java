package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.forgotPassword.ForgotPasswordRequestDTO;
import com.cogent.cogentappointment.admin.service.ForgotPasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-09-20
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.BASE_PASSWORD)
@Api(SwaggerConstants.ForgotPasswordConstant.BASE_API_VALUE)
public class ForgotPasswordResource {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordResource(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping(WebResourceKeyConstants.ForgotPasswordConstants.FORGOT)
    @ApiOperation(SwaggerConstants.ForgotPasswordConstant.FORGOT_PASSWORD_OPERATION)
    public ResponseEntity<?> forgotPassword(@RequestParam(name = "username") String username) {
        forgotPasswordService.forgotPassword(username);
        return ok().build();
    }

    @GetMapping(WebResourceKeyConstants.ForgotPasswordConstants.VERIFY)
    @ApiOperation(SwaggerConstants.ForgotPasswordConstant.VERIFY_RESET_CODE)
    public ResponseEntity<?> verify(@RequestParam(name = "resetCode") String resetCode) {
        forgotPasswordService.verify(resetCode);
        return ok().build();
    }

    @PutMapping
    @ApiOperation(SwaggerConstants.ForgotPasswordConstant.UPDATE_PASSWORD)
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) {
        forgotPasswordService.updatePassword(requestDTO);
        return ok().build();
    }

}
