package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.exception.utils.ValidationUtils;
import com.cogent.cogentappointment.admin.model.Hospital;
import com.cogent.cogentappointment.admin.model.HospitalContactNumber;
import com.cogent.cogentappointment.admin.repository.HospitalLogoRepository;
import com.cogent.cogentappointment.admin.utils.commons.NameAndCodeValidationUtils;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.CommonLogConstant;
import com.cogent.cogentappointment.admin.log.constants.HospitalLog;
import com.cogent.cogentappointment.admin.model.HospitalLogo;
import com.cogent.cogentappointment.admin.repository.HospitalContactNumberRepository;
import com.cogent.cogentappointment.admin.repository.HospitalRepository;
import com.cogent.cogentappointment.admin.service.FileService;
import com.cogent.cogentappointment.admin.service.HospitalService;
import com.cogent.cogentappointment.admin.utils.HospitalUtils;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author smriti ON 12/01/2020
 */
@Service
@Transactional
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    private final HospitalContactNumberRepository hospitalContactNumberRepository;

    private final HospitalLogoRepository hospitalLogoRepository;

    private final FileService fileService;

    private final Validator validator;

    public HospitalServiceImpl(HospitalRepository hospitalRepository,
                               HospitalContactNumberRepository hospitalContactNumberRepository,
                               HospitalLogoRepository hospitalLogoRepository,
                               FileService fileService,
                               Validator validator) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalContactNumberRepository = hospitalContactNumberRepository;
        this.hospitalLogoRepository = hospitalLogoRepository;
        this.fileService = fileService;
        this.validator = validator;
    }

    @Override
    public void save(@Valid HospitalRequestDTO requestDTO, MultipartFile multipartFile) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, HospitalLog.HOSPITAL);

        ValidationUtils.validateConstraintViolation(validator.validate(requestDTO));

        List<Object[]> hospitals = hospitalRepository.validateHospitalDuplicity(
                requestDTO.getName(), requestDTO.getHospitalCode());

        NameAndCodeValidationUtils.validateDuplicity(hospitals, requestDTO.getName(), requestDTO.getHospitalCode(),
                Hospital.class.getSimpleName());

        Hospital hospital = save(HospitalUtils.convertDTOToHospital(requestDTO));

        saveHospitalContactNumber(hospital.getId(), requestDTO.getContactNumber());

        saveHospitalLogo(hospital, multipartFile);

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, HospitalLog.HOSPITAL, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(HospitalUpdateRequestDTO updateRequestDTO, MultipartFile multipartFile) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, HospitalLog.HOSPITAL);

        Hospital hospital = findById(updateRequestDTO.getId());

        List<Object[]> hospitals = hospitalRepository.validateHospitalDuplicityForUpdate(
                updateRequestDTO.getId(), updateRequestDTO.getName(), updateRequestDTO.getHospitalCode());

        NameAndCodeValidationUtils.validateDuplicity(hospitals, updateRequestDTO.getName(),
                hospital.getCode(), Hospital.class.getSimpleName());

        HospitalUtils.parseToUpdatedHospital(updateRequestDTO, hospital);

        updateHospitalContactNumber(hospital.getId(), updateRequestDTO.getContactNumberUpdateRequestDTOS());

        updateHospitalLogo(hospital, multipartFile);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, HospitalLog.HOSPITAL, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO hospitalSearchRequestDTO, Pageable pageable) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SEARCHING_PROCESS_STARTED, HospitalLog.HOSPITAL);

        List<HospitalMinimalResponseDTO> responseDTOS = hospitalRepository.search(hospitalSearchRequestDTO, pageable);

        log.info(CommonLogConstant.SEARCHING_PROCESS_COMPLETED, HospitalLog.HOSPITAL, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.DELETING_PROCESS_STARTED, HospitalLog.HOSPITAL);

        Hospital hospital = findById(deleteRequestDTO.getId());

        HospitalUtils.parseToDeletedHospital(hospital, deleteRequestDTO);

        log.info(CommonLogConstant.DELETING_PROCESS_COMPLETED, HospitalLog.HOSPITAL, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public Hospital fetchActiveHospital(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED, HospitalLog.HOSPITAL);

        Hospital hospital = hospitalRepository.findActiveHospitalById(id)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(CommonLogConstant.FETCHING_PROCESS_COMPLETED, HospitalLog.HOSPITAL, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return hospital;
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchHospitalForDropDown() {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, HospitalLog.HOSPITAL);

        List<HospitalDropdownResponseDTO> responseDTOS = hospitalRepository.fetchActiveHospitalForDropDown();

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, HospitalLog.HOSPITAL, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long hospitalId) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, HospitalLog.HOSPITAL);

        HospitalResponseDTO responseDTO = hospitalRepository.fetchDetailsById(hospitalId);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, HospitalLog.HOSPITAL, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private Hospital save(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    private void saveHospitalContactNumber(Long hospitalId, List<String> contactNumbers) {
        List<HospitalContactNumber> hospitalContactNumbers = contactNumbers.stream()
                .map(contactNumber -> HospitalUtils.parseToHospitalContactNumber(hospitalId, contactNumber))
                .collect(Collectors.toList());

        saveHospitalContactNumber(hospitalContactNumbers);
    }

    private void saveHospitalContactNumber(List<HospitalContactNumber> hospitalContactNumbers) {
        hospitalContactNumberRepository.saveAll(hospitalContactNumbers);
    }

    private void saveHospitalLogo(Hospital hospital, MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{files});
            saveHospitalLogo(HospitalUtils.convertFileToHospitalLogo(responseList.get(0), hospital));
        }
    }

    private List<FileUploadResponseDTO> uploadFiles(Hospital hospital, MultipartFile[] files) {
        String subDirectory = hospital.getClass().getSimpleName() + StringConstant.FORWARD_SLASH + hospital.getName();
        return fileService.uploadFiles(files, subDirectory);
    }

    private void saveHospitalLogo(HospitalLogo hospitalLogo) {
        hospitalLogoRepository.save(hospitalLogo);
    }

    private Hospital findById(Long id) {
        return hospitalRepository.findHospitalById(id).orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private void updateHospitalContactNumber(Long hospitalId,
                                             List<HospitalContactNumberUpdateRequestDTO> updateRequestDTOS) {

        List<HospitalContactNumber> hospitalContactNumbers = updateRequestDTOS.stream()
                .map(requestDTO -> HospitalUtils.parseToUpdatedHospitalContactNumber(hospitalId, requestDTO))
                .collect(Collectors.toList());

        saveHospitalContactNumber(hospitalContactNumbers);
    }

    public void updateHospitalLogo(Hospital hospital, MultipartFile files) {
        HospitalLogo hospitalLogo = hospitalLogoRepository.findHospitalLogoByHospitalId(hospital.getId());

        if (Objects.isNull(hospitalLogo)) saveHospitalLogo(hospital, files);
        else updateHospitalLogo(hospital, hospitalLogo, files);
    }

    private void updateHospitalLogo(Hospital hospital, HospitalLogo hospitalLogo, MultipartFile files) {

        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{files});
            HospitalUtils.setFileProperties(responseList.get(0), hospitalLogo);
        } else
            hospitalLogo.setStatus(StatusConstants.INACTIVE);
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };

}
