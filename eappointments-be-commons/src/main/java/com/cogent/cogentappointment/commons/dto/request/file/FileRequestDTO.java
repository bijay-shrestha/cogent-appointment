package com.cogent.cogentappointment.commons.dto.request.file;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/28-1:25 PM
 */
@Getter
@Builder
@Setter
public class FileRequestDTO implements Serializable{

    private String fileName;

    private String url;

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String expiryTime;
}
