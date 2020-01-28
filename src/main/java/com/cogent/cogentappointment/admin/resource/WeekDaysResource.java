package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.service.WeekDaysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 25/11/2019
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.WeekDaysConstants.BASE_WEEK_DAYS)
@Api(SwaggerConstants.WeekDaysConstant.BASE_API_VALUE)
public class WeekDaysResource {

    private final WeekDaysService weekDaysService;

    public WeekDaysResource(WeekDaysService weekDaysService) {
        this.weekDaysService = weekDaysService;
    }

    @GetMapping
    @ApiOperation(SwaggerConstants.WeekDaysConstant.FETCH_ACTIVE_WEEK_DAYS)
    public ResponseEntity<?> fetchActiveWeekDays() {
        return ok(weekDaysService.fetchActiveWeekDays());
    }
}
