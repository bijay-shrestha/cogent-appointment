package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.commons.dto.request.file.FileRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;

/**
 * @author rupak ON 2020/06/28-11:42 AM
 */
@RestController
@RequestMapping(API_V1 +"/file")
public class MinIOResource {

    private final MinIOService minIOService;

    public MinIOResource(MinIOService minIOService) {
        this.minIOService = minIOService;
    }

    @PutMapping
    public ResponseEntity<?> getPresignedObjectURL(@RequestBody FileRequestDTO fileRequestDTO) {

        String url = minIOService.getPresignedObjectURL(fileRequestDTO);

        return new ResponseEntity<>(url, HttpStatus.OK);

    }
}
