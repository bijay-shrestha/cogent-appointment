package com.cogent.cogentappointment.commons.dto.jasper;

import lombok.*;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author rupak ON 2020/07/10-12:06 PM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JasperReportDownloadResponse implements Serializable {

    private InputStream inputStream;
    private String fileName;
}
