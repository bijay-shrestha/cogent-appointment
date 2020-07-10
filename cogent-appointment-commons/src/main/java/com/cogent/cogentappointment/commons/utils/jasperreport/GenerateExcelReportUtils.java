package com.cogent.cogentappointment.commons.utils.jasperreport;

import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.commons.utils.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/07/09-2:25 PM
 */
public class GenerateExcelReportUtils {

    public static JasperReportDownloadResponse generateExcelReport(List<?> cList,
                                                                   Map hParam,
                                                                   String reportPath) throws FileNotFoundException, JRException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // READ TEMPLATE AS INPUT STREAM
        InputStream fileRead = new FileInputStream(reportPath);

        JasperDesign design = JRXmlLoader.load(fileRead);
        JasperReport report = JasperCompileManager.compileReport(design);

        JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(cList);
        JasperPrint print = JasperFillManager.fillReport(report, hParam, jrbcds);

        String fileName = getTimeInMillisecondsFromLocalDate() + ".xlsx";

        JRXlsxExporter xlsExporter = new JRXlsxExporter();
        xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//            xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE, xlsFile);
        xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        xlsExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);

        xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);

        xlsExporter.exportReport();

        // Create an Input Stream from the bytes extracted by the OutputStream
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        JasperReportDownloadResponse downloadResponse = JasperReportDownloadResponse.builder()
                .fileName(fileName)
                .inputStream(inputStream)
                .build();

        return downloadResponse;
    }
}
