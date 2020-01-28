package com.cogent.cogentappointment.admin.dto.response.files;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti on 2019-08-27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponseDTO implements Serializable {
    private String fileUri;

    private String fileType;

    private Long fileSize;
}
