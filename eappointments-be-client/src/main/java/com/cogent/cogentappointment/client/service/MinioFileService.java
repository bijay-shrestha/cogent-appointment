package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Rupak
 */
public interface MinioFileService {

    void addAttachment(MultipartFile file);

    List<FileUploadResponseDTO> addAttachmentIntoSubDirectory(String subDirectory, MultipartFile[] file);

//    List<Item> getAllList() throws MinioException;
//
//    InputStream getObject(String object) throws MinioException;
//
//    InputStream getObjectWithSubDirectory(String subDirectory, String object) throws MinioException;
//
//    void deleteFiles(String object) throws MinioException;

}
