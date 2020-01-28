package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.admin.service.QualificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 11/11/2019
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.QualificationConstants.BASE_QUALIFICATION)
@Api(SwaggerConstants.QualificationConstant.BASE_API_VALUE)
public class QualificationResource {

    private final QualificationService qualificationService;

    public QualificationResource(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @PostMapping
    @ApiOperation(SwaggerConstants.QualificationConstant.SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody QualificationRequestDTO requestDTO) {
        qualificationService.save(requestDTO);
        return created(create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.QualificationConstants.BASE_QUALIFICATION)).build();
    }

    @PutMapping
    @ApiOperation(SwaggerConstants.QualificationConstant.UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody QualificationUpdateRequestDTO updateRequestDTO) {
        qualificationService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.QualificationConstant.DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        qualificationService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.QualificationConstant.SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody QualificationSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(qualificationService.search(searchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.ACTIVE + WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.QualificationConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<List<QualificationDropdownDTO>> fetchQualificationForDropDown() {
        return ok(qualificationService.fetchActiveQualificationForDropDown());
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.QualificationConstant.DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(qualificationService.fetchDetailsById(id));
    }
}
