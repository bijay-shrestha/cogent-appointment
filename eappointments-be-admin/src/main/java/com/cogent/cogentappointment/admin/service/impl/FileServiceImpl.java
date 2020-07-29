package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.configuration.FileConfiguration;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.admin.service.FileService;
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

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.FileServiceMessages.*;
import static com.cogent.cogentappointment.admin.constants.StringConstant.FORWARD_SLASH;
import static com.cogent.cogentappointment.admin.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.admin.log.constants.FileLog.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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
            String path = StringUtil.urlConverter(subDirectoryLocation, HYPHEN, FORWARD_SLASH) + FORWARD_SLASH;
            Path file = load(path + fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                log.info(LOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
                return resource;
            } else {
                log.error(INVALID_FILE_TYPE_MESSAGE + fileName);
                throw new NoContentFoundException(INVALID_FILE_TYPE_MESSAGE + fileName);
            }
        } catch (MalformedURLException e) {
            log.error(INVALID_FILE_TYPE_MESSAGE + fileName);
            throw new NoContentFoundException(INVALID_FILE_TYPE_MESSAGE + fileName);
        }
    }

    private FileUploadResponseDTO uploadFile(MultipartFile file, String subDirectoryLocation) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

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

        log.info(UPLOADING_FILE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }


//    public MultipartFile renameFile(MultipartFile file) {
//
//
//
//
//        DiskFileItem fileItem = new DiskFileItem("file", "image/png", false, file.getName(),
//                (int) file.length(), file.getParentFile());
//        fileItem.getOutputStream();
//
//
//        MultipartFile renamedFile = null;
//        renamedFile = new CommonsMultipartFile(FilenameUtils.getBaseName(file.getOriginalFilename()).concat(getTimeInMillisecondsFromLocalDate().toString()) + "." + FilenameUtils.getExtension(file.getOriginalFilename()), file.getInputStream());
//
//        return renamedFile;
//    }

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
            if (file.isEmpty()){
                log.error(FILES_EMPTY_MESSAGE + filename);
                throw new NoContentFoundException(FILES_EMPTY_MESSAGE + filename);
            }


            if (filename.contains("..")){
                log.error(INVALID_FILE_SEQUENCE);
                throw new BadRequestException(INVALID_FILE_SEQUENCE);
            }


            resolvePath(subDirectory);

//            Files.copy(file.getInputStream(),
//                    this.rootLocation.resolve(filename.concat(getTimeInMillisecondsFromLocalDate().toString())),
//                    StandardCopyOption.REPLACE_EXISTING);

            Files.copy(file.getInputStream(),
                    this.rootLocation.resolve(getTimeInMillisecondsFromLocalDate().toString()),
                    StandardCopyOption.REPLACE_EXISTING);



            return filename;
        } catch (IOException exception) {
            log.error(FILE_EXCEPTION);
            throw new OperationUnsuccessfulException(FILE_EXCEPTION);
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
