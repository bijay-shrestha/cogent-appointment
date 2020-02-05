package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.constants.StatusConstants;
import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.*;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.model.Hospital;
import com.cogent.cogentappointment.client.model.HospitalBanner;
import com.cogent.cogentappointment.client.model.HospitalContactNumber;
import com.cogent.cogentappointment.client.model.HospitalLogo;
import com.cogent.cogentappointment.client.repository.HospitalBannerRepository;
import com.cogent.cogentappointment.client.repository.HospitalContactNumberRepository;
import com.cogent.cogentappointment.client.repository.HospitalLogoRepository;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.service.FileService;
import com.cogent.cogentappointment.client.service.HospitalService;
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

import static com.cogent.cogentappointment.client.constants.StringConstant.FORWARD_SLASH;
import static com.cogent.cogentappointment.client.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.client.utils.HospitalUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.NameAndCodeValidationUtils.validateDuplicity;

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

    private final HospitalBannerRepository hospitalBannerRepository;

    private final FileService fileService;

    private final Validator validator;

    public HospitalServiceImpl(HospitalRepository hospitalRepository,
                               HospitalContactNumberRepository hospitalContactNumberRepository,
                               HospitalLogoRepository hospitalLogoRepository,
                               HospitalBannerRepository hospitalBannerRepository,
                               FileService fileService,
                               Validator validator) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalContactNumberRepository = hospitalContactNumberRepository;
        this.hospitalLogoRepository = hospitalLogoRepository;
        this.hospitalBannerRepository = hospitalBannerRepository;
        this.fileService = fileService;
        this.validator = validator;
    }

    @Override
    public void save(@Valid HospitalRequestDTO requestDTO, MultipartFile logo, MultipartFile banner) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL);

        validateConstraintViolation(validator.validate(requestDTO));

        List<Object[]> hospitals = hospitalRepository.validateHospitalDuplicity(
                requestDTO.getName(), requestDTO.getHospitalCode());

        validateDuplicity(hospitals, requestDTO.getName(), requestDTO.getHospitalCode(),
                Hospital.class.getSimpleName());

        Hospital hospital = save(convertDTOToHospital(requestDTO));

        saveHospitalContactNumber(hospital.getId(), requestDTO.getContactNumber());

        saveHospitalLogo(hospital, logo);

        saveHospitalBanner(hospital, banner);

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(HospitalUpdateRequestDTO updateRequestDTO, MultipartFile logo, MultipartFile banner) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = findById(updateRequestDTO.getId());

        List<Object[]> hospitals = hospitalRepository.validateHospitalDuplicityForUpdate(
                updateRequestDTO.getId(), updateRequestDTO.getName(), updateRequestDTO.getHospitalCode());

        validateDuplicity(hospitals, updateRequestDTO.getName(),
                hospital.getCode(), Hospital.class.getSimpleName());

        parseToUpdatedHospital(updateRequestDTO, hospital);

        updateHospitalContactNumber(hospital.getId(), updateRequestDTO.getContactNumberUpdateRequestDTOS());

        updateHospitalLogo(hospital, logo);

        updateHospitalBanner(hospital, banner);

        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO hospitalSearchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL);

        List<HospitalMinimalResponseDTO> responseDTOS = hospitalRepository.search(hospitalSearchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = findById(deleteRequestDTO.getId());

        parseToDeletedHospital(hospital, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public Hospital fetchActiveHospital(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = hospitalRepository.findActiveHospitalById(id)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return hospital;
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchHospitalForDropDown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, HOSPITAL);

        List<HospitalDropdownResponseDTO> responseDTOS = hospitalRepository.fetchActiveHospitalForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<HospitalMinResponseDTO> fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL);

        List<HospitalMinResponseDTO> responseDTO = hospitalRepository.fetchMinDetails(searchRequestDTO);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL);

        HospitalResponseDTO responseDTO = hospitalRepository.fetchDetailsById(hospitalId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private Hospital save(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    private void saveHospitalContactNumber(Long hospitalId, List<String> contactNumbers) {
        List<HospitalContactNumber> hospitalContactNumbers = contactNumbers.stream()
                .map(contactNumber -> parseToHospitalContactNumber(hospitalId, contactNumber))
                .collect(Collectors.toList());

        saveHospitalContactNumber(hospitalContactNumbers);
    }

    private void saveHospitalContactNumber(List<HospitalContactNumber> hospitalContactNumbers) {
        hospitalContactNumberRepository.saveAll(hospitalContactNumbers);
    }

    private void saveHospitalLogo(Hospital hospital, MultipartFile logo) {
        if (!Objects.isNull(logo)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{logo});
            saveHospitalLogo(convertFileToHospitalLogo(responseList.get(0), hospital));
        }
    }

    private void saveHospitalBanner(Hospital hospital, MultipartFile hospitalBanner) {
        if (!Objects.isNull(hospitalBanner)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{hospitalBanner});
            saveHospitalBanner(convertFileToHospitalHospitalBanner(responseList.get(0), hospital));
        }
    }

    private List<FileUploadResponseDTO> uploadFiles(Hospital hospital, MultipartFile[] files) {
        String subDirectory = hospital.getClass().getSimpleName() + FORWARD_SLASH + hospital.getName();
        return fileService.uploadFiles(files, subDirectory);
    }

    private void saveHospitalLogo(HospitalLogo hospitalLogo) {
        hospitalLogoRepository.save(hospitalLogo);
    }

    private void saveHospitalBanner(HospitalBanner hospitalBanner) {
        hospitalBannerRepository.save(hospitalBanner);
    }

    private Hospital findById(Long id) {
        return hospitalRepository.findHospitalById(id).orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private void updateHospitalContactNumber(Long hospitalId,
                                             List<HospitalContactNumberUpdateRequestDTO> updateRequestDTOS) {

        List<HospitalContactNumber> hospitalContactNumbers = updateRequestDTOS.stream()
                .map(requestDTO -> parseToUpdatedHospitalContactNumber(hospitalId, requestDTO))
                .collect(Collectors.toList());

        saveHospitalContactNumber(hospitalContactNumbers);
    }

    public void updateHospitalLogo(Hospital hospital, MultipartFile logo) {
        HospitalLogo hospitalLogo = hospitalLogoRepository.findHospitalLogoByHospitalId(hospital.getId());

        if (Objects.isNull(hospitalLogo)) saveHospitalLogo(hospital, logo);
        else updateHospitalLogo(hospital, hospitalLogo, logo);
    }

    private void updateHospitalLogo(Hospital hospital, HospitalLogo hospitalLogo, MultipartFile logo) {

        if (!Objects.isNull(logo)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{logo});
            setFileProperties(responseList.get(0), hospitalLogo);
        } else
            hospitalLogo.setStatus(StatusConstants.INACTIVE);
    }

    private void updateHospitalBanner(Hospital hospital, HospitalBanner hospitalBanner, MultipartFile banner) {

        if (!Objects.isNull(banner)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{banner});
            setFileProperties(responseList.get(0), hospitalBanner);
        } else
            hospitalBanner.setStatus(StatusConstants.INACTIVE);
    }

    public void updateHospitalBanner(Hospital hospital, MultipartFile banner) {
        HospitalBanner hospitalBanner = hospitalBannerRepository.findHospitalBannerByHospitalId(hospital.getId());

        if (Objects.isNull(hospitalBanner)) saveHospitalBanner(hospital, banner);
        else updateHospitalBanner(hospital, hospitalBanner, banner);
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };

}
