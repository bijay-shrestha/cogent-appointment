package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.configuration.FileConfiguration;
import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.admin.service.FileService;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.admin.utils.commons.StringUtil;
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

import static com.cogent.cogentappointment.admin.constants.StringConstant.FORWARD_SLASH;
import static com.cogent.cogentappointment.admin.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.admin.log.constants.FileLog.*;

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
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();
        log.info(LOADING_FILE_PROCESS_STARTED);

        try {
            String path = StringUtil.urlConverter(subDirectoryLocation, HYPHEN, FORWARD_SLASH) + FORWARD_SLASH;
            Path file = load(path + fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                log.info(LOADING_FILE_PROCESS_COMPLETED, DateUtils.getDifferenceBetweenTwoTime(startTime));
                return resource;
            } else {
                throw new NoContentFoundException(ErrorMessageConstants.FileServiceMessages.INVALID_FILE_TYPE_MESSAGE + fileName);
            }
        } catch (MalformedURLException e) {
            throw new NoContentFoundException(ErrorMessageConstants.FileServiceMessages.INVALID_FILE_TYPE_MESSAGE + fileName);
        }
    }

    private FileUploadResponseDTO uploadFile(MultipartFile file, String subDirectoryLocation) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        String fileName = store(file, subDirectoryLocation);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(storageProperties.fileBasePath)
                .path(StringUtil.urlConverter(subDirectoryLocation, FORWARD_SLASH, HYPHEN))
                .path(FORWARD_SLASH)
                .path(fileName)
                .toUriString();

        FileUploadResponseDTO responseDTO = FileUploadResponseDTO.builder()
                .fileUri(fileUri)
                .fileType(file.getContentType())
                .fileSize(file.getSize()).build();

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<FileUploadResponseDTO> uploadFiles(MultipartFile[] files,
                                                   String subDirectoryLocation) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(UPLOADING_FILE_PROCESS_STARTED);

        List<FileUploadResponseDTO> fileUploadResponseDTOS = Arrays.stream(files)
                .map(file -> uploadFile(file, subDirectoryLocation))
                .collect(Collectors.toList());

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, DateUtils.getDifferenceBetweenTwoTime(startTime));

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
                FORWARD_SLASH + subDirectoryLocation + FORWARD_SLASH);

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
