package com.cogent.cogentappointment.commons.utils.jasperreport;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.commons.utils.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/07/09-1:14 PM
 */
public class GeneratePDFUtils {

    public static void generatePDFReport(List<?> cList,
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

        JRPdfExporter exporter = new JRPdfExporter();

        // Here we assign the parameters jp and baos to the exporter
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

        try {
            exporter.exportReport();

            String reportPath = "./reports/";
            Files.createDirectories(Paths.get(reportPath));


            OutputStream printOut = new FileOutputStream(
                    new File(reportPath  + "-" +
                            getTimeInMillisecondsFromLocalDate() +
                            ".pdf"));

            printOut.write(baos.toByteArray());


            baos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
