package com.cogent.cogentappointment.admin.utils.jasperreport;

import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.utils.jasperreport.GeneratePDFUtils.generatePDFReport;

/**
 * @author rupak ON 2020/07/09-1:07 PM
 */
@Service
public class JasperUtils {

    public static void generateJasperReport(List<?> listData, Map hParam) throws JRException,
            IOException {

        generatePDFReport(listData, hParam);


    }


}