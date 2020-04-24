package com.cogent.cogentappointment.logging.resource;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.service.AdminLogService;
import com.cogent.cogentappointment.logging.service.ClientLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.logging.constants.SwaggerConstants.AdminLogConstant.*;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.AdminLogConstants.BASE_ADMIN_LOG;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.ClientLogConstants.BASE_CLIENT;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Rupak
 */
@RestController
@RequestMapping(value = API_V1 + BASE_ADMIN_LOG)
@Api(BASE_API_VALUE)
public class AdminLogResource {

    private final AdminLogService adminLogService;
    private final ClientLogService clientLogService;

    public AdminLogResource(AdminLogService adminLogService, ClientLogService clientLogService) {
        this.adminLogService = adminLogService;
        this.clientLogService = clientLogService;
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody AdminLogSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(adminLogService.search(searchRequestDTO, pageable));
    }

    @PutMapping(USERMENU_LOG_STATICS)
    @ApiOperation(USER_MENU_STATICS_OPERATION)
    public ResponseEntity<?> fetchUserMenuLogsStatics(@RequestBody AdminLogSearchRequestDTO searchRequestDTO,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok().body(adminLogService.fetchUserMenuLogsStatics(searchRequestDTO, pageable));
    }

    @PutMapping(USERMENU_LOG_DIAGRAM)
    @ApiOperation(USER_MENU_LOG_DIAGRAM_OPERATION)
    public ResponseEntity<?> fetchUserMenuLogStaticsForDiagram(@RequestBody AdminLogSearchRequestDTO searchRequestDTO) {

        return ok().body(adminLogService.fetchUserMenuLogsStaticsforDiagram(searchRequestDTO));
    }

    @PutMapping(BASE_CLIENT + SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody ClientLogSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(clientLogService.searchByClientId(searchRequestDTO, pageable));
    }

    @PutMapping(BASE_CLIENT + USERMENU_LOG_STATICS)
    @ApiOperation(USER_MENU_STATICS_OPERATION)
    public ResponseEntity<?> fetchUserMenuLogsStatics(@RequestBody ClientLogSearchRequestDTO searchRequestDTO,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok().body(clientLogService.fetchUserMenuLogsStaticsByClientId(searchRequestDTO, pageable));
    }

    @PutMapping(BASE_CLIENT + USERMENU_LOG_DIAGRAM)
    @ApiOperation(USER_MENU_LOG_DIAGRAM_OPERATION)
    public ResponseEntity<?> fetchUserMenuLogsStaticsforDiagramByClientId(@RequestBody ClientLogSearchRequestDTO searchRequestDTO) {

        return ok().body(clientLogService.fetchUserMenuLogsStaticsforDiagramByClientId(searchRequestDTO));
    }


}
