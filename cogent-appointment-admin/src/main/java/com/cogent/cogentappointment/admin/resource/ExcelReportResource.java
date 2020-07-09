package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.service.ExcelReportService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.ExcelReportConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.TRANSACTION_LOG;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ExcelReportConstants.BASE_EXCEL_REPORT;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak ON 2020/07/09-12:43 PM
 */
@RestController
@RequestMapping(value = API_V1 + BASE_EXCEL_REPORT)
@Api(BASE_API_VALUE)
public class ExcelReportResource {

    private final ExcelReportService excelReportService;

    public ExcelReportResource(ExcelReportService excelReportService) {
        this.excelReportService = excelReportService;
    }


    @PutMapping(TRANSACTION_LOG)
    public ResponseEntity<?> generateTransactionLogExcelReport(@RequestBody TransactionLogSearchDTO searchRequestDTO,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size) throws Exception {

        Pageable pageable = PageRequest.of(page, size);
        excelReportService.generateTransactionLogReport(searchRequestDTO,pageable);

        return ok().build();


    }
}
