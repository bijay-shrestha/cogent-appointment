package com.cogent.cogentappointment.admin.utils.jasperreport;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;
import net.sf.jasperreports.export.XlsxExporterConfiguration;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/07/09-2:25 PM
 */
public class GenerateExcelReportUtils {

    public static void generateExcelReport(List<?> cList,
                                         Map hParam) throws FileNotFoundException, JRException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // LOCATION OF JASPER REPOTR TEMPLATE FILE.
        String string = "././reporting/Reports.jrxml";

        // READ TEMPLATE AS INPUT STREAM
        InputStream fileRead = new FileInputStream(string);

        JasperDesign design = JRXmlLoader.load(fileRead);
        JasperReport report = JasperCompileManager.compileReport(design);

        JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(cList);
        JasperPrint print = JasperFillManager.fillReport(report, hParam, jrbcds);

        String reportDestination = "./reports/"+getTimeInMillisecondsFromLocalDate() + ".xlsx";  //This is generated Correctly

//        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        IOUtils.copy(fis, response.getOutputStream());
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment; filename=" + "OutStanding_DC_Report" + ".xlsx"); //This is downloaded as .xhtml
//        response.flushBuffer();
//        fis.close();


        try {


            File xlsFile = new File(reportDestination);

            JRXlsxExporter Xlsxexporter = new JRXlsxExporter();

            Xlsxexporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            Xlsxexporter.setParameter(JRExporterParameter.OUTPUT_FILE, xlsFile);

            SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
            xlsReportConfiguration.setOnePagePerSheet(false);
            xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
            xlsReportConfiguration.setDetectCellType(false);
            xlsReportConfiguration.setWhitePageBackground(false);
            Xlsxexporter.setConfiguration((XlsxExporterConfiguration) xlsReportConfiguration);

            Xlsxexporter.exportReport();//File is generated Correctly


            FileInputStream fis = new FileInputStream(new File(reportDestination));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
