package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.FILE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 12/01/2020
 */
@RestController
@RequestMapping(API_V1)
public class FileResource {

    private final FileService fileService;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(FILE)
    public ResponseEntity<Resource> downloadFile(@PathVariable String subDirectoryLocation,
                                                 @PathVariable String filename) {

        Resource file = fileService.loadAsResource(subDirectoryLocation, filename);

        return ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + StringConstant.BACKWARD_SLASH + file.getFilename() + StringConstant.BACKWARD_SLASH)
                .body(file);
    }
}
