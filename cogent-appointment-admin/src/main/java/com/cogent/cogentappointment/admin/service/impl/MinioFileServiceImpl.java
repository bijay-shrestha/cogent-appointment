package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.configuration.MinioStorageConfig;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.service.MinioFileService;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StringConstant.FORWARD_SLASH;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.MinioFileConstants.BASE_FILE;
import static com.cogent.cogentappointment.admin.log.constants.FileLog.UPLOADING_FILE_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.constants.FileLog.UPLOADING_FILE_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Rupak
 */
@Service
@Slf4j
public class MinioFileServiceImpl implements MinioFileService {

    private final MinioService minioService;
    private final MinioStorageConfig minioStorageConfig;

    public MinioFileServiceImpl(MinioService minioService, MinioStorageConfig minioStorageConfig) {
        this.minioService = minioService;
        this.minioStorageConfig = minioStorageConfig;
    }


    @Override
    public void addAttachment(MultipartFile file) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        Path path = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
        log.info(UPLOADING_FILE_PROCESS_STARTED);
        try {
            minioService.upload(path, file.getInputStream(), file.getContentType());
        } catch (MinioException e) {
            log.error("The file cannot be upload on the internal storage." +
                    " Please retry later");
            throw new IllegalStateException("The file cannot be upload on the internal storage." +
                    " Please retry later", e);
        } catch (IOException e) {
            log.error("The file cannot be read");
            throw new IllegalStateException("The file cannot be read", e);
        }

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

    }

    @Override
    public List<FileUploadResponseDTO> addAttachmentIntoSubDirectory(String subDirectoryLocation,
                                                                     MultipartFile[] files) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        List<FileUploadResponseDTO> fileUploads =
                Arrays.stream(files).map(file -> uploadFile(subDirectoryLocation, file)).collect(Collectors.toList());

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));


        return fileUploads;
    }

    private FileUploadResponseDTO uploadFile(String subDirectoryLocation, MultipartFile file) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        String renamedFileName = renameFile(file.getOriginalFilename());

        Path path = Paths.get(subDirectoryLocation + FORWARD_SLASH + renamedFileName);
        log.info("The designated path is :: {}", path);
        try {
            minioService.upload(path, file.getInputStream(), file.getContentType());
        } catch (MinioException e) {
            log.error("The file cannot be upload on the internal storage." +
                    " Please retry later");
            throw new IllegalStateException("The file cannot be upload on the internal storage." +
                    " Please retry later", e);
        } catch (IOException e) {
            log.error("The file cannot be read");
            throw new IllegalStateException("The file cannot be read", e);
        }

        String fileUri = minioStorageConfig.getServerlocation() + API_V1 + BASE_FILE + FORWARD_SLASH + path.toString();

        FileUploadResponseDTO responseDTO = FileUploadResponseDTO.builder()
                .fileUri(fileUri)
                .fileType(file.getContentType())
                .fileSize(file.getSize()).build();

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private String renameFile(String originalFilename) {

//        String fileName = FilenameUtils.getName(originalFilename);
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        return getTimeInMillisecondsFromLocalDate().toString().concat(".").concat(fileExtension);
    }

    @Override
    public List<Item> getAllList() throws MinioException {

        return minioService.list();
    }

    @Override
    public InputStream getObject(String object) throws MinioException {

        InputStream inputStream = minioService.get(Paths.get(object));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        return inputStream;
    }

    @Override
    public InputStream getObjectWithSubDirectory(String subDirectory, String object) throws MinioException {

        InputStream inputStream = minioService.get((Paths.get(subDirectory + "/" + object)));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        return inputStream;
    }

    @Override
    public void deleteFiles(String object) throws MinioException {

        minioService.remove(Paths.get(object));
    }
}
