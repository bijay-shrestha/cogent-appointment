package com.cogent.cogentappointment.commons.service.impl;

import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import com.cogent.cogentappointment.commons.dto.request.file.FileRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author rupak ON 2020/06/28-12:47 PM
 */
@Service
@Transactional
public class MinIOServiceImpl implements MinIOService {

    private final MinIOProperties minIOProperties;

    public MinIOServiceImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    @Override
    public String getPresignedObjectURL(FileRequestDTO fileRequestDTO) {

        try {

            MinioClient minioClient = new MinioClient(minIOProperties.getURL(),
                    minIOProperties.getACCESS_KEY(),
                    minIOProperties.getSECRET_KEY());

            return minioClient.getPresignedObjectUrl(Method.GET, minIOProperties.getBUCKET_NAME(),
                    fileRequestDTO.getFileName(),
                    Integer.parseInt(minIOProperties.getEXPIRY_TIME()), null);

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
