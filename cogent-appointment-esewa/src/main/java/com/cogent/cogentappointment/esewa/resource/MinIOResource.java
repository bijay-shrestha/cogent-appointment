package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.commons.dto.request.file.FileURLRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import com.cogent.cogentappointment.esewa.dto.request.DataWrapperRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.MinioResourceConstant.*;
import static com.cogent.cogentappointment.esewa.utils.JWTDecryptUtils.toDecrypt;
import static com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils.convertValue;

/**
 * @author rupak ON 2020/06/28-11:42 AM
 */
@RestController
@RequestMapping(API_V1 + BASE_FILE)
public class MinIOResource {

    private final MinIOService minIOService;

    private final DataWrapperRequest dataWrapperRequest;

    public MinIOResource(MinIOService minIOService, DataWrapperRequest dataWrapperRequest) {
        this.minIOService = minIOService;
        this.dataWrapperRequest = dataWrapperRequest;
    }

    @PutMapping(PUT_PERSIGNED_URL)
    public ResponseEntity<?> putPresignedObjectURL() throws IOException {

        FileURLRequestDTO fileURLRequestDTO = convertValue(dataWrapperRequest.getData(),
                FileURLRequestDTO.class);

        String url = minIOService.putPresignedObjectURL(fileURLRequestDTO);
        return new ResponseEntity<>(url, HttpStatus.OK);

    }

    @PutMapping(GET_PERSIGNED_URL)
    public ResponseEntity<?> getPresignedObjectURL() throws IOException {

        FileURLRequestDTO fileURLRequestDTO = convertValue(dataWrapperRequest.getData(),
                FileURLRequestDTO.class);

        String url = minIOService.getPresignedObjectURL(fileURLRequestDTO);
        return new ResponseEntity<>(url, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<?> getObjectURL(@RequestParam("fileUri") String fileUri) {

        String objectUrl = minIOService.getObjectUrl(fileUri);

        return new ResponseEntity<>(objectUrl, HttpStatus.OK);

    }
}
