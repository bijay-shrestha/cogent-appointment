package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.constants.SwaggerConstants;
import com.cogent.cogentappointment.client.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.client.service.QualificationAliasService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 11/11/2019
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.QualificationAliasConstants.BASE_QUALIFICATION_ALIAS)
@Api(SwaggerConstants.QualificationAliasConstant.BASE_API_VALUE)
public class QualificationAliasResource {

    private final QualificationAliasService qualificationAliasService;

    public QualificationAliasResource(QualificationAliasService qualificationAliasService) {
        this.qualificationAliasService = qualificationAliasService;
    }

    @GetMapping
    @ApiOperation(SwaggerConstants.QualificationAliasConstant.FETCH_ACTIVE_QUALIFICATION_ALIAS)
    public ResponseEntity<?> fetchActiveQualificationAlias() {
        return ok(qualificationAliasService.fetchActiveQualificationAlias());
    }
}
