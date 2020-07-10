package com.cogent.cogentappointment.admin.utils.jasperreport;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/07/09-2:25 PM
 */
public class GenerateExcelReportUtils {

    public static void generateExcelReport(List<?> cList,
                                           Map hParam, String reportPath) throws FileNotFoundException, JRException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // READ TEMPLATE AS INPUT STREAM
        InputStream fileRead = new FileInputStream(reportPath);

        JasperDesign design = JRXmlLoader.load(fileRead);
        JasperReport report = JasperCompileManager.compileReport(design);

        JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(cList);
        JasperPrint print = JasperFillManager.fillReport(report, hParam, jrbcds);

        String reportDestination = "./reports/" + getTimeInMillisecondsFromLocalDate() + ".xlsx";  //This is generated Correctly

//        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        IOUtils.copy(fis, response.getOutputStream());
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment; filename=" + "OutStanding_DC_Report" + ".xlsx"); //This is downloaded as .xhtml
//        response.flushBuffer();
//        fis.close();


        try {

            File xlsFile = new File(reportDestination);

            JRXlsxExporter xlsExporter = new JRXlsxExporter();

            xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE, xlsFile);

            xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            xlsExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);

            xlsExporter.exportReport();


            FileInputStream fis = new FileInputStream(new File(reportDestination));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
