package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.commons.dto.request.file.FileURLRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;

/**
 * @author rupak ON 2020/06/28-11:42 AM
 */
@RestController
@RequestMapping(API_V1 + "/file")
public class MinIOResource {

    private final MinIOService minIOService;

    public MinIOResource(MinIOService minIOService) {
        this.minIOService = minIOService;
    }


    @PutMapping
    public ResponseEntity<?> getPresignedObjectURL(@RequestBody FileURLRequestDTO fileURLRequestDTO) {

        String url = minIOService.getPresignedObjectURL(fileURLRequestDTO);

        return new ResponseEntity<>(url, HttpStatus.OK);

    }
}
