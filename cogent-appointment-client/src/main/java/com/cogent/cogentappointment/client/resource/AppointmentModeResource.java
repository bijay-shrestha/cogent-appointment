package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.AppointmentModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentModeConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentModeConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentModeConstants.BASE_APPOINTMENT_MODE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.MIN;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_APPOINTMENT_MODE)
@Api(BASE_API_VALUE)
public class AppointmentModeResource {

    private final AppointmentModeService service;

    public AppointmentModeResource(AppointmentModeService service) {
        this.service = service;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AppointmentModeRequestDTO requestDTO) {
        service.save(requestDTO);
        return created(create(API_V1 + BASE_APPOINTMENT_MODE)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody AppointmentModeUpdateRequestDTO updateRequestDTO) {
        service.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        service.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody AppointmentModeSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(service.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(service.fetchDetailsById(id));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveAppointmentMode() {
        return ok(service.fetchActiveMinAppointmentMode());
    }
}
