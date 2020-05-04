package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualificationAlias.QualificationAliasRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualificationAlias.QualificationAliasSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualificationAlias.QualificationAliasUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.QualificationAliasService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.QualificationAliasConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.QualificationAliasConstants.BASE_QUALIFICATION_ALIAS;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 11/11/2019
 */
@RestController
@RequestMapping(API_V1 + BASE_QUALIFICATION_ALIAS)
@Api(BASE_API_VALUE)
public class QualificationAliasResource {

    private final QualificationAliasService qualificationAliasService;

    public QualificationAliasResource(QualificationAliasService qualificationAliasService) {
        this.qualificationAliasService = qualificationAliasService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody QualificationAliasRequestDTO aliasRequestDTO) {
        qualificationAliasService.save(aliasRequestDTO);
        return created(create(API_V1 + BASE_QUALIFICATION_ALIAS)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody QualificationAliasUpdateRequestDTO updateRequestDTO) {
        qualificationAliasService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        qualificationAliasService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody QualificationAliasSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(qualificationAliasService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_ACTIVE_QUALIFICATION_ALIAS)
    public ResponseEntity<?> fetchActiveQualificationAlias() {
        return ok(qualificationAliasService.fetchActiveQualificationAlias());
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_ACTIVE_QUALIFICATION_ALIAS)
    public ResponseEntity<?> fetchQualificationAlias() {
        return ok(qualificationAliasService.fetchQualificationAlias());
    }
}
