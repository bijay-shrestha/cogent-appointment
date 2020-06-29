//package com.cogent.cogentappointment.esewa.service.impl;
//
//import com.cogent.cogentappointment.esewa.service.MinioFileService;
//import com.jlefebure.spring.boot.minio.MinioException;
//import com.jlefebure.spring.boot.minio.MinioService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//import java.nio.file.Paths;
//
///**
// * @author Rupak
// */
//@Service
//@Slf4j
//public class MinioFileServiceImpl implements MinioFileService {
//
//    private final MinioService minioService;
//
//    public MinioFileServiceImpl(MinioService minioService) {
//        this.minioService = minioService;
//    }
//
//    @Override
//    public InputStream getObjectWithSubDirectory(String subDirectory, String object) throws MinioException {
//
//        InputStream inputStream = minioService.get((Paths.get(subDirectory + "/" + object)));
//        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
//
//        return inputStream;
//    }
//}
