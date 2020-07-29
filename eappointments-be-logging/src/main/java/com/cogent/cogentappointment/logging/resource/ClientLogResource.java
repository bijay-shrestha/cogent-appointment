package com.cogent.cogentappointment.logging.resource;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.service.ClientLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.logging.constants.SwaggerConstants.AdminLogConstant.*;
import static com.cogent.cogentappointment.logging.constants.SwaggerConstants.ClientLogConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.ClientLogConstants.BASE_CLIENT;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.ClientLogConstants.BASE_CLIENT_LOG;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Rupak
 */
@RestController
@RequestMapping(value = API_V1 + BASE_CLIENT_LOG)
@Api(BASE_API_VALUE)
public class ClientLogResource {

    private final ClientLogService clientLogService;

    public ClientLogResource(ClientLogService clientLogService) {
        this.clientLogService = clientLogService;
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody ClientLogSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(clientLogService.search(searchRequestDTO, pageable));
    }

    @PutMapping(USERMENU_LOG_STATICS)
    @ApiOperation(USER_MENU_STATICS_OPERATION)
    public ResponseEntity<?> fetchUserMenuLogsStatics(@RequestBody ClientLogSearchRequestDTO searchRequestDTO,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok().body(clientLogService.fetchUserMenuLogsStatics(searchRequestDTO, pageable));
    }

    @PutMapping(USERMENU_LOG_DIAGRAM)
    @ApiOperation(USER_MENU_LOG_DIAGRAM_OPERATION)
    public ResponseEntity<?> fetchUserMenuLogsStaticsforDiagram(@RequestBody ClientLogSearchRequestDTO searchRequestDTO) {


        return ok().body(clientLogService.fetchUserMenuLogsStaticsforDiagram(searchRequestDTO));
    }

}
