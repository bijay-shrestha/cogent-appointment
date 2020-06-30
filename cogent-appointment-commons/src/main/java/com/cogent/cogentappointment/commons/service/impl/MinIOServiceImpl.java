package com.cogent.cogentappointment.commons.service.impl;

import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import com.cogent.cogentappointment.commons.dto.request.file.FileURLRequestDTO;
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
import static io.minio.http.Method.PUT;

/**
 * @author rupak ON 2020/06/28-12:47 PM
 */
@Service
@Slf4j
@Transactional
public class MinIOServiceImpl implements MinIOService {

    private final MinIOProperties minIOProperties;

    public MinIOServiceImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    @Override
    public String putPresignedObjectURL(FileURLRequestDTO fileRequestDTO) {

        try {

            MinioClient minioClient = new MinioClient(minIOProperties.getURL(),
                    minIOProperties.getACCESS_KEY(),
                    minIOProperties.getSECRET_KEY());

            String url = minioClient.getPresignedObjectUrl(PUT, minIOProperties.getBUCKET_NAME(),
                    fileRequestDTO.getFileName(),
                    Integer.parseInt(minIOProperties.getEXPIRY_TIME()),
                    null);

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

    @Override
    public String getPresignedObjectURL(FileURLRequestDTO fileRequestDTO) {
        try {

            MinioClient minioClient = new MinioClient(minIOProperties.getURL(),
                    minIOProperties.getACCESS_KEY(),
                    minIOProperties.getSECRET_KEY());

            String url = minioClient.getPresignedObjectUrl(GET, minIOProperties.getBUCKET_NAME(),
                    fileRequestDTO.getFileName(),
                    Integer.parseInt(minIOProperties.getEXPIRY_TIME()),
                    null);

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
