package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.QualificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.QualificationConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.QualificationConstants.BASE_QUALIFICATION;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 11/11/2019
 */
@RestController
@RequestMapping(API_V1 + BASE_QUALIFICATION)
@Api(BASE_API_VALUE)
public class QualificationResource {

    private final QualificationService qualificationService;

    public QualificationResource(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody QualificationRequestDTO requestDTO) {
        qualificationService.save(requestDTO);
        return created(create(API_V1 + BASE_QUALIFICATION)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody QualificationUpdateRequestDTO updateRequestDTO) {
        qualificationService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        qualificationService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody QualificationSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(qualificationService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<List<DropDownResponseDTO>> fetchQualificationForDropDown() {
        return ok(qualificationService.fetchMinActiveQualification());
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(qualificationService.fetchDetailsById(id));
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchMinQualification() {
        return ok(qualificationService.fetchMinQualification());
    }
}
