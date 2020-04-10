package com.cogent.cogentappointment.esewa.service;

import com.jlefebure.spring.boot.minio.MinioException;

import java.io.InputStream;

/**
 * @author Rupak
 */
public interface MinioFileService {

    InputStream getObjectWithSubDirectory(String subDirectory, String object) throws MinioException;



}
