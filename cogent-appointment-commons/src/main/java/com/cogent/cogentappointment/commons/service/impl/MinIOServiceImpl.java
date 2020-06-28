package com.cogent.cogentappointment.commons.service.impl;

import com.cogent.cogentappointment.commons.dto.request.file.FileRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static io.minio.http.Method.GET;

/**
 * @author rupak ON 2020/06/28-12:47 PM
 */
@Service
@Slf4j
@Transactional
public class MinIOServiceImpl implements MinIOService {

    @Override
    public String getPresignedObjectURL(FileRequestDTO fileRequestDTO) {

        try {

            MinioClient minioClient = new MinioClient("uat-presigned.cogenthealth.com.np",9000,
                    fileRequestDTO.getAccessKey(),
                    fileRequestDTO.getSecretKey());

//            MinioClient minioClient = new MinioClient("https://play.minio.io:9000",
//                    "Q3AM3UQ867SPQQA43P2F",
//                    "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
//
//            String url = minioClient.getObjectUrl("my-bucketname", "my-objectname");

            String url = minioClient.getPresignedObjectUrl(GET,fileRequestDTO.getBucket(),
                    fileRequestDTO.getFileName(),
                    3*60, null);
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
