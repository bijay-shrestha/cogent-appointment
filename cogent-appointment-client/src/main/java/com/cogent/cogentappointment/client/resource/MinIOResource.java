package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.configuration.MinIOProperties;
import com.cogent.cogentappointment.client.dto.request.file.FileURLRequestDTO;
import com.cogent.cogentappointment.commons.dto.request.file.FileRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;

/**
 * @author rupak ON 2020/06/28-11:42 AM
 */
@RestController
@RequestMapping(API_V1 + "/file")
public class MinIOResource {

    private final MinIOService minIOService;
    private final MinIOProperties minIOProperties;

    public MinIOResource(MinIOService minIOService, MinIOProperties minIOProperties) {
        this.minIOService = minIOService;
        this.minIOProperties = minIOProperties;
    }

    @PutMapping
    public ResponseEntity<?> getPresignedObjectURL(@RequestBody FileURLRequestDTO fileURLRequestDTO) {

        FileRequestDTO requestDTO = FileRequestDTO.builder()
                .accessKey(minIOProperties.getACCESS_KEY())
                .secretKey(minIOProperties.getSECRET_KEY())
                .url(minIOProperties.getURL())
                .bucket(minIOProperties.getBUCKET_NAME())
                .fileName(fileURLRequestDTO.getFileName())
                .expiryTime(minIOProperties.getEXPIRY_TIME())
                .build();

        String url = minIOService.getPresignedObjectURL(requestDTO);

        return new ResponseEntity<>(url, HttpStatus.OK);

    }
}
