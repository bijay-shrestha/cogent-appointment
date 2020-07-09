package com.cogent.cogentappointment.client.resource;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.ExcelReportConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.ExcelReportConstants.BASE_EXCEL_REPORT;

/**
 * @author rupak ON 2020/07/09-12:43 PM
 */
@RestController
@RequestMapping(value = API_V1 + BASE_EXCEL_REPORT)
@Api(BASE_API_VALUE)
public class ExcelResource {
}
