package com.cogent.cogentappointment.commons.utils.jasperreport;

import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import com.cogent.cogentappointment.commons.exception.InternalServerErrorException;
import com.cogent.cogentappointment.commons.log.CommonLogConstant;
import com.cogent.cogentappointment.commons.utils.FileResourceUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.commons.log.CommonLogConstant.EXCEL_REPORT_DOWNLOAD;
import static com.cogent.cogentappointment.commons.log.CommonLogConstant.EXCEL_REPORT_DOWNLOAD_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.commons.utils.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.commons.utils.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/07/09-2:25 PM
 */
@Slf4j
public class GenerateExcelReportUtils {

    public static JasperReportDownloadResponse generateExcelReport(List<?> cList,
                                                                   Map hParam,
                                                                   String reportPath) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.EXCEL_REPORT_DOWNLOAD_PROCESS_STARTED, EXCEL_REPORT_DOWNLOAD);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // READ TEMPLATE AS INPUT STREAM
        InputStream fileRead = null;
        try {

            fileRead = new FileInputStream
                    (new FileResourceUtils().convertResourcesFileIntoFile(reportPath));
        } catch (FileNotFoundException e) {

            throw new InternalServerErrorException(InputStream.class, "Report Template Not Found");
        }

        JasperDesign design = null;
        JasperPrint print = null;
        JasperReport report = null;
        JasperReportDownloadResponse downloadResponse = null;

        JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(cList);

        try {
            design = JRXmlLoader.load(fileRead);
            report = JasperCompileManager.compileReport(design);
            print = JasperFillManager.fillReport(report, hParam, jrbcds);

            String fileName = getTimeInMillisecondsFromLocalDate() + ".xlsx";

            JRXlsxExporter exporter = new JRXlsxExporter();

            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setWhitePageBackground(false);
            configuration.setRemoveEmptySpaceBetweenColumns(true);
            configuration.setRemoveEmptySpaceBetweenRows(true);

            exporter.setConfiguration(configuration);
            exporter.exportReport();

            // Create an Input Stream from the bytes extracted by the OutputStream
            InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

            downloadResponse = JasperReportDownloadResponse.builder()
                    .fileName(fileName)
                    .inputStream(inputStream)
                    .build();


        } catch (JRException e) {
            log.error(EXCEL_REPORT_DOWNLOAD, e.getMessage());
            e.printStackTrace();
        }


        log.info(EXCEL_REPORT_DOWNLOAD_PROCESS_COMPLETED, EXCEL_REPORT_DOWNLOAD, getDifferenceBetweenTwoTime(startTime));

        return downloadResponse;
    }
}
