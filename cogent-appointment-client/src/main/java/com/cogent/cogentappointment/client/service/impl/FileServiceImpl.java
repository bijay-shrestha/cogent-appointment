package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.configuration.FileConfiguration;
import com.cogent.cogentappointment.client.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.client.constants.StringConstant;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.client.service.FileService;
import com.cogent.cogentappointment.client.utils.commons.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.log.constants.FileLog.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 2019-08-27
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private Path rootLocation;

    private final FileConfiguration storageProperties;

    public FileServiceImpl(FileConfiguration storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation());
        this.storageProperties = storageProperties;
    }

    @Override
    public Resource loadAsResource(String subDirectoryLocation, String fileName) {
        Long startTime = getTimeInMillisecondsFromLocalDate();
        log.info(LOADING_FILE_PROCESS_STARTED);

        try {
            String path = StringUtil.urlConverter(subDirectoryLocation, StringConstant.HYPHEN, StringConstant.FORWARD_SLASH) + StringConstant.FORWARD_SLASH;
            Path file = load(path + fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                log.info(LOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
                return resource;
            } else {
                throw new NoContentFoundException(ErrorMessageConstants.FileServiceMessages.INVALID_FILE_TYPE_MESSAGE + fileName);
            }
        } catch (MalformedURLException e) {
            throw new NoContentFoundException(ErrorMessageConstants.FileServiceMessages.INVALID_FILE_TYPE_MESSAGE + fileName);
        }
    }

    private FileUploadResponseDTO uploadFile(MultipartFile file, String subDirectoryLocation) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        String fileName = store(file, subDirectoryLocation);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(storageProperties.fileBasePath)
                .path(StringUtil.urlConverter(subDirectoryLocation, StringConstant.FORWARD_SLASH, StringConstant.HYPHEN))
                .path(StringConstant.FORWARD_SLASH)
                .path(fileName)
                .toUriString();

        FileUploadResponseDTO responseDTO = FileUploadResponseDTO.builder()
                .fileUri(fileUri)
                .fileType(file.getContentType())
                .fileSize(file.getSize()).build();

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<FileUploadResponseDTO> uploadFiles(MultipartFile[] files,
                                                   String subDirectoryLocation) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        List<FileUploadResponseDTO> fileUploadResponseDTOS = Arrays.stream(files)
                .map(file -> uploadFile(file, subDirectoryLocation))
                .collect(Collectors.toList());

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return fileUploadResponseDTOS;
    }

    @Override
    public String store(MultipartFile file, String subDirectory) {

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (file.isEmpty())
                throw new NoContentFoundException(ErrorMessageConstants.FileServiceMessages.FILES_EMPTY_MESSAGE + filename);

            if (filename.contains(".."))
                throw new BadRequestException(ErrorMessageConstants.FileServiceMessages.INVALID_FILE_SEQUENCE);

            resolvePath(subDirectory);

            Files.copy(file.getInputStream(),
                    this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException exception) {
            throw new OperationUnsuccessfulException(ErrorMessageConstants.FileServiceMessages.FILE_EXCEPTION);
        }
    }

    private void resolvePath(String subDirectoryLocation) throws IOException {
        Path path = Paths.get(storageProperties.getLocation() +
                StringConstant.FORWARD_SLASH + subDirectoryLocation + StringConstant.FORWARD_SLASH);

        /*test
        Path path = Paths.get(subDirectoryLocation);
        **/
        Files.createDirectories(path);
        this.rootLocation = path.toAbsolutePath().normalize();
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

}
