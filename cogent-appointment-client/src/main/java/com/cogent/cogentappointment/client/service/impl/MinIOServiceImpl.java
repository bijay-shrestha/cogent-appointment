package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.configuration.MinIOProperties;
import com.cogent.cogentappointment.client.dto.request.file.FileURLRequestDTO;
import com.cogent.cogentappointment.client.service.MinIOService;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author rupak ON 2020/06/28-12:47 PM
 */
@Servicecl
@Slf4j
@Transactional
public class MinIOServiceImpl implements MinIOService {

    private final MinIOProperties minIOProperties;

    public MinIOServiceImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    @Override
    public String getPresignedObjectURL(FileURLRequestDTO fileRequestDTO) {

        try {

            //test

            //            MinioClient minioClient = new MinioClient("https://play.minio.io:9000",
//                    "Q3AM3UQ867SPQQA43P2F",
//                    "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
//
//            String url = minioClient.getObjectUrl("my-bucketname", "my-objectname");

            MinioClient minioClient = new MinioClient("https://uat-presigned.cogenthealth.com.np",
                    minIOProperties.getACCESS_KEY(),
                    minIOProperties.getSECRET_KEY());

            String url = minioClient.presignedGetObject(minIOProperties.getBUCKET_NAME(),
                    fileRequestDTO.getFileName());

//            String url = minioClient.getObjectUrl(minIOProperties.getBUCKET_NAME(),
//                    fileRequestDTO.getFileName());
//                    Integer.parseInt(fileRequestDTO.getExpiryTime()), null);

            log.info("MinIO Error {}::", url);

            return url;

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        return null;
    }
}
