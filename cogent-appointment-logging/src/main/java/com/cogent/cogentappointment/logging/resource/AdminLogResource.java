package com.cogent.cogentappointment.logging.resource;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.service.AdminLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.logging.constants.SwaggerConstants.AdminLogConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.logging.constants.SwaggerConstants.AdminLogConstant.SEARCH_OPERATION;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.AdminLogConstants.BASE_ADMIN_LOG;
import static com.cogent.cogentappointment.logging.constants.WebResourceKeyConstants.SEARCH;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Rupak
 */
@RestController
@RequestMapping(value = API_V1 + BASE_ADMIN_LOG)
@Api(BASE_API_VALUE)
public class AdminLogResource {

    private final AdminLogService adminLogService;

    public AdminLogResource(AdminLogService adminLogService) {
        this.adminLogService = adminLogService;
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody AdminLogSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(adminLogService.search(searchRequestDTO, pageable));
    }
}
