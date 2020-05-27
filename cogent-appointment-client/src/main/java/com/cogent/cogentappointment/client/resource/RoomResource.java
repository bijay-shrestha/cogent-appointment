package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.RoomConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RoomConstants.BASE_ROOM;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 5/19/20
 */

@RestController
@RequestMapping(API_V1 + BASE_ROOM)
@Api(BASE_API_VALUE)
public class RoomResource {

    private final RoomService roomService;

    public RoomResource(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody RoomRequestDTO requestDTO) {
        roomService.save(requestDTO);
        return created(create(API_V1 + BASE_ROOM)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody RoomUpdateRequestDTO updateRequestDTO) {
        roomService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        roomService.delete(deleteRequestDTO);
        return ok().build();
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_ACTIVE_ROOM_FOR_DROP_DOWN)
    public ResponseEntity<?> fetchActiveMinRoom() {
        return ok(roomService.fetchActiveMinRoom());
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_ROOM_FOR_DROP_DOWN)
    public ResponseEntity<?> fetchMinRoom() {
        return ok(roomService.fetchMinRoom());
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody RoomSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(roomService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ACTIVE + MIN + HOSPITAL_DEPARTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ACTIVE_ROOM_FOR_DROP_DOWN_BY_HOSPITAL_DEPARTMENT_ID)
    public ResponseEntity<?> fetchActiveMinRoomByHospitalDepartmentId(@PathVariable("hospitalDepartmentId") Long hospitalDepartmentId) {
        return ok(roomService.fetchActiveMinRoomByHospitalDepartmentId(hospitalDepartmentId));
    }

    @GetMapping(MIN + HOSPITAL_DEPARTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ROOM_FOR_DROP_DOWN_BY_HOSPITAL_DEPARTMENT_ID)
    public ResponseEntity<?> fetchMinRoomByHospitalDepartmentId(@PathVariable("hospitalDepartmentId") Long hospitalDepartmentId) {
        return ok(roomService.fetchMinRoomByHospitalDepartmentId(hospitalDepartmentId));
    }

}
