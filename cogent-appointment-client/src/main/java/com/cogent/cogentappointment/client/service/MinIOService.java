package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.file.FileURLRequestDTO;

/**
 * @author rupak ON 2020/06/28-12:47 PM
 */
public interface MinIOService {

     String getPresignedObjectURL(FileURLRequestDTO fileRequestDTO);
}
