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

            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minIOProperties.getURL())
                            .credentials(minIOProperties.getACCESS_KEY(),
                                    minIOProperties.getSECRET_KEY())
                            .build();

            String url = minioClient.presignedPutObject(minIOProperties.getBUCKET_NAME(),
                    fileRequestDTO.getFileName(),
                    Integer.parseInt(minIOProperties.getEXPIRY_TIME()));

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

            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minIOProperties.getURL())
                            .credentials(minIOProperties.getACCESS_KEY(),
                                    minIOProperties.getSECRET_KEY())
                            .build();

            String url = minioClient.presignedGetObject(minIOProperties.getBUCKET_NAME(),
                    fileRequestDTO.getFileName(),
                    Integer.parseInt(minIOProperties.getEXPIRY_TIME()));

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
    public String getObjectUrl(String fileUri) {

        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minIOProperties.getURL())
                            .credentials(minIOProperties.getACCESS_KEY(),
                                    minIOProperties.getSECRET_KEY())
                            .build();

            String objectUrl = minioClient.getObjectUrl(minIOProperties.getBUCKET_NAME(),
                    fileUri);

            log.info("MinIO Error {}::", objectUrl);

            return objectUrl;

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

//    private MinioClient createMinioClient() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException, RegionConflictException {
//
//        MinioClient minioClient =
//                MinioClient.builder ()
//                        .endpoint ( minIOProperties.getURL () )
//                        .credentials ( minIOProperties.getACCESS_KEY (),
//                                minIOProperties.getSECRET_KEY () )
//                        .build ();
//
//        // Check if the bucket already exists.
//        boolean isExist =
//                minioClient.bucketExists ( BucketExistsArgs.builder ().bucket ( minIOProperties.getBUCKET_NAME () ).build () );
//        if (isExist) {
//            System.out.println ( "Bucket already exists." );
//        } else {
//            // Make a new bucket called asiatrip to hold a zip file of photos.
//            minioClient.makeBucket ( MakeBucketArgs.builder ().bucket ( minIOProperties.getBUCKET_NAME () ).build () );
//        }
//        return minioClient;
//    }


}
