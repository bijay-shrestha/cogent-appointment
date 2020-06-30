package com.cogent.cogentappointment.commons.service;

import com.cogent.cogentappointment.commons.dto.request.file.FileRequestDTO;

/**
 * @author rupak ON 2020/06/28-12:47 PM
 */
public interface MinIOService {

     String getPresignedObjectURL(FileRequestDTO fileRequestDTO);
}
