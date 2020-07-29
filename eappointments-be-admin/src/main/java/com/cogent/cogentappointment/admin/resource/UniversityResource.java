package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversitySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.UniversityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.UniversityConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.UniversityConstants.BASE_UNIVERSITY;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 30/01/2020
 */
@RestController
@RequestMapping(value = API_V1 + BASE_UNIVERSITY)
@Api(BASE_API_VALUE)
public class UniversityResource {

    private final UniversityService universityService;

    public UniversityResource(UniversityService universityService) {
        this.universityService = universityService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody UniversityRequestDTO requestDTO) {
        universityService.save(requestDTO);
        return created(create(API_V1 + BASE_UNIVERSITY)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody UniversityUpdateRequestDTO updateRequestDTO) {
        universityService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        universityService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody UniversitySearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(universityService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(universityService.fetchDetailsById(id));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_ACTIVE_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveUniversity() {
        return ok(universityService.fetchActiveUniversity());
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchUniversity() {
        return ok(universityService.fetchUniversity());
    }
}
