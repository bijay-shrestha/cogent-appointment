package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.forgotPassword.ForgotPasswordRequestDTO;
import com.cogent.cogentappointment.admin.service.ForgotPasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.ForgotPasswordConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.BASE_PASSWORD;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.VERIFY;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-09-20
 */
@RestController
@RequestMapping(API_V1 + BASE_PASSWORD)
@Api(BASE_API_VALUE)
public class ForgotPasswordResource {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordResource(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping(FORGOT)
    @ApiOperation(FORGOT_PASSWORD_OPERATION)
    public ResponseEntity<?> forgotPassword(@RequestParam(name = "username") String username) {
        forgotPasswordService.forgotPassword(username);
        return ok().build();
    }

    @GetMapping(VERIFY)
    @ApiOperation(VERIFY_RESET_CODE)
    public ResponseEntity<?> verify(@RequestParam(name = "resetCode") String resetCode) {
        forgotPasswordService.verify(resetCode);
        return ok().build();
    }

    @PutMapping
    @ApiOperation(UPDATE_PASSWORD)
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) {
        forgotPasswordService.updatePassword(requestDTO);
        return ok().build();
    }

}
