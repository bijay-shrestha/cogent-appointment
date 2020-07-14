package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.service.ExcelReportService;
import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLConnection;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.ExcelReportConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ExcelReportConstants.BASE_EXCEL_REPORT;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.PatientConstant.PATIENT_DETAILS;
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


    @PutMapping(APPOINTMENT_LOG)
    @ApiOperation(DOWNLOAD_EXCEL_APPOINTMENT_LOG)
    public ResponseEntity<?> generateAppointmentLogExcelReport(@RequestBody AppointmentLogSearchDTO searchRequestDTO,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               HttpServletResponse response) throws IOException {

        Pageable pageable = PageRequest.of(page, size);

        JasperReportDownloadResponse downloadResponse = excelReportService
                .generateAppointmentLogExcelReport(searchRequestDTO, pageable);


        response.addHeader("Content-disposition", "attachment;filename=" + downloadResponse.getFileName());

        response.setContentType(URLConnection.guessContentTypeFromName(downloadResponse.getFileName()));

        IOUtils.copy(downloadResponse.getInputStream(), response.getOutputStream());

        response.flushBuffer();

        return ok().build();
    }

    @PutMapping(RESCHEDULE_LOG)
    @ApiOperation(DOWNLOAD_EXCEL_RESCHEDULE_LOG)
    public ResponseEntity<?> generateRescheduleLogExcelReport(@RequestBody AppointmentRescheduleLogSearchDTO searchRequestDTO,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              HttpServletResponse response) throws Exception {

        Pageable pageable = PageRequest.of(page, size);
        JasperReportDownloadResponse downloadResponse = excelReportService
                .generateRescheduleLogExcelReport(searchRequestDTO, pageable);

        response.addHeader("Content-disposition", "attachment;filename=" + downloadResponse.getFileName());

        response.setContentType(URLConnection.guessContentTypeFromName(downloadResponse.getFileName()));

        IOUtils.copy(downloadResponse.getInputStream(), response.getOutputStream());

        response.flushBuffer();

        return ok().build();
    }

    @PutMapping(TRANSACTION_LOG)
    @ApiOperation(DOWNLOAD_EXCEL_TRANSACTION_LOG)
    public ResponseEntity<?> generateTransactionLogExcelReport(@RequestBody TransactionLogSearchDTO searchRequestDTO,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               HttpServletResponse response) throws Exception {

        Pageable pageable = PageRequest.of(page, size);
        JasperReportDownloadResponse downloadResponse = excelReportService.generateTransactionLogReport(searchRequestDTO, pageable);

        response.addHeader("Content-disposition", "attachment;filename=" + downloadResponse.getFileName());

        response.setContentType(URLConnection.guessContentTypeFromName(downloadResponse.getFileName()));

        IOUtils.copy(downloadResponse.getInputStream(), response.getOutputStream());

        response.flushBuffer();

        return ok().build();


    }

    @PutMapping(TRANSFER_LOG)
    @ApiOperation(DOWNLOAD_EXCEL_TRANSFER_LOG)
    public ResponseEntity<?> generateAppointmentTransferLogExcelReport(@Valid
                                                                       @RequestBody
                                                                               AppointmentTransferSearchRequestDTO searchRequestDTO,
                                                                       @RequestParam("page") int page,
                                                                       @RequestParam("size") int size,
                                                                       HttpServletResponse response) throws Exception {

        Pageable pageable = PageRequest.of(page, size);
        JasperReportDownloadResponse downloadResponse = excelReportService.generateAppointmentTransferLogReport(searchRequestDTO, pageable);

        response.addHeader("Content-disposition", "attachment;filename=" + downloadResponse.getFileName());
        response.setContentType(URLConnection.guessContentTypeFromName(downloadResponse.getFileName()));

        IOUtils.copy(downloadResponse.getInputStream(), response.getOutputStream());
        response.flushBuffer();

        return ok().build();
    }

    @PutMapping(PATIENT_DETAILS)
    @ApiOperation(DOWNLOAD_EXCEL_PATIENT_DETAILS)
    public ResponseEntity<?> generatePatientDetailsExcelReport(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               HttpServletResponse response) throws Exception {

        Pageable pageable = PageRequest.of(page, size);

        JasperReportDownloadResponse downloadResponse = excelReportService.
                generatePatientDetailsExcelReport(searchRequestDTO, pageable);

        response.addHeader("Content-disposition", "attachment;filename=" + downloadResponse.getFileName());

        response.setContentType(URLConnection.guessContentTypeFromName(downloadResponse.getFileName()));

        IOUtils.copy(downloadResponse.getInputStream(), response.getOutputStream());

        response.flushBuffer();

        return ok().build();


    }

}
