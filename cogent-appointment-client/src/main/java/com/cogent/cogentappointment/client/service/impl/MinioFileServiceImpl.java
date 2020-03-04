package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.configuration.MinioStorageConfiguration;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.client.service.MinioFileService;
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

import static com.cogent.cogentappointment.client.constants.StringConstant.FORWARD_SLASH;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.FILES;
import static com.cogent.cogentappointment.client.log.constants.FileLog.UPLOADING_FILE_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.constants.FileLog.UPLOADING_FILE_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Rupak
 */
@Service
@Slf4j
public class MinioFileServiceImpl implements MinioFileService {

    private final MinioService minioService;
    private final MinioStorageConfiguration minioStorageConfiguration;

    public MinioFileServiceImpl(MinioService minioService, MinioStorageConfiguration minioStorageConfiguration) {
        this.minioService = minioService;
        this.minioStorageConfiguration = minioStorageConfiguration;
    }


    @Override
    public void addAttachment(MultipartFile file) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String randomPath = startTime.toString() + "." + suffix;


        Path path = Paths.get(Objects.requireNonNull(randomPath));
        try {
            minioService.upload(path, file.getInputStream(), file.getContentType());
        } catch (MinioException e) {
            throw new IllegalStateException("The file cannot be upload on the internal storage." +
                    " Please retry later", e);
        } catch (IOException e) {
            throw new IllegalStateException("The file cannot be read", e);
        }

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

    }

    @Override
    public List<FileUploadResponseDTO> addAttachmentIntoSubDirectory(String subDirectoryLocation,
                                                                     MultipartFile[] files) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        List<FileUploadResponseDTO> fileUploads = Arrays.stream(files)
                .map(file -> uploadFile(subDirectoryLocation, file))
                .collect(Collectors.toList());

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
            throw new IllegalStateException("The file cannot be upload on the internal storage." +
                    " Please retry later", e);
        } catch (IOException e) {
            throw new IllegalStateException("The file cannot be read", e);
        }

        String fileUri = minioStorageConfiguration.getServerlocation() + API_V1 + FILES + FORWARD_SLASH + path.toString();

        FileUploadResponseDTO responseDTO = FileUploadResponseDTO.builder()
                .fileUri(fileUri)
                .fileType(file.getContentType())
                .fileSize(file.getSize()).build();

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
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

    private String renameFile(String originalFilename) {

//        String fileName = FilenameUtils.getName(originalFilename);
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        return getTimeInMillisecondsFromLocalDate().toString().concat(".").concat(fileExtension);
    }

}
