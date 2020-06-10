package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.*;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentServiceTypeService;
import com.cogent.cogentappointment.admin.service.HospitalService;
import com.cogent.cogentappointment.admin.service.MinioFileService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.Validator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.ALIAS_NOT_FOUND;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NO_RECORD_FOUND;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.BillingModeLog.BILLING_MODE;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.*;
import static com.cogent.cogentappointment.admin.utils.HmacApiInfoUtils.parseToHmacApiInfo;
import static com.cogent.cogentappointment.admin.utils.HmacApiInfoUtils.updateHmacApiInfoAsHospital;
import static com.cogent.cogentappointment.admin.utils.HospitalBillingModeInfoUtils.deleteHospitalBillingModeInfoList;
import static com.cogent.cogentappointment.admin.utils.HospitalBillingModeInfoUtils.parseToHospitalBillingModeInfoList;
import static com.cogent.cogentappointment.admin.utils.HospitalUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

    private final HmacApiInfoRepository hmacApiInfoRepository;

    private final HospitalBillingModeInfoRepository hospitalBillingModeInfoRepository;

    private final BillingModeRepository billingModeRepository;

    private final MinioFileService minioFileService;

    private final Validator validator;

    private final HospitalAppointmentServiceTypeRepository hospitalAppointmentServiceTypeRepository;


    private final AppointmentServiceTypeService appointmentServiceTypeService;

    public HospitalServiceImpl(HospitalRepository hospitalRepository,
                               HospitalContactNumberRepository hospitalContactNumberRepository,
                               HospitalLogoRepository hospitalLogoRepository,
                               HospitalBannerRepository hospitalBannerRepository,
                               HmacApiInfoRepository hmacApiInfoRepository,
                               HospitalBillingModeInfoRepository hospitalBillingModeInfoRepository,
                               BillingModeRepository billingModeRepository,
                               MinioFileService minioFileService,
                               Validator validator,
                               HospitalAppointmentServiceTypeRepository hospitalAppointmentServiceTypeRepository,
                               AppointmentServiceTypeService appointmentServiceTypeService) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalContactNumberRepository = hospitalContactNumberRepository;
        this.hospitalLogoRepository = hospitalLogoRepository;
        this.hospitalBannerRepository = hospitalBannerRepository;
        this.hmacApiInfoRepository = hmacApiInfoRepository;
        this.hospitalBillingModeInfoRepository = hospitalBillingModeInfoRepository;
        this.billingModeRepository = billingModeRepository;
        this.minioFileService = minioFileService;
        this.validator = validator;
        this.hospitalAppointmentServiceTypeRepository = hospitalAppointmentServiceTypeRepository;
        this.appointmentServiceTypeService = appointmentServiceTypeService;
    }


    @Override
    public void save(@Valid HospitalRequestDTO requestDTO, MultipartFile logo, MultipartFile banner)
            throws NoSuchAlgorithmException {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL);

        validateConstraintViolation(validator.validate(requestDTO));

        List<Object[]> hospitals = hospitalRepository.validateHospitalDuplicity(
                requestDTO.getName(), requestDTO.getEsewaMerchantCode(), requestDTO.getAlias());

        validateDuplicity(hospitals, requestDTO.getName(),
                requestDTO.getEsewaMerchantCode(),
                requestDTO.getAlias(),
                CLIENT
        );

        Hospital hospital = save(convertDTOToHospital(requestDTO));

        saveHospitalContactNumber(hospital.getId(), requestDTO.getContactNumber());

        saveHospitalLogo(hospital, logo);

        saveHospitalBanner(hospital, banner);

        saveHmacApiInfo(parseToHmacApiInfo(hospital));

        if (requestDTO.getBillingModeId().size() > 0)
            saveHospitalBillingModeInfo(hospital, requestDTO.getBillingModeId());

        saveHospitalAppointmentServiceType(hospital,
                requestDTO.getAppointmentServiceTypeIds(),
                requestDTO.getPrimaryAppointmentServiceTypeId()
        );

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(@Valid HospitalUpdateRequestDTO updateRequestDTO, MultipartFile logo, MultipartFile banner) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL);

        validateConstraintViolation(validator.validate(updateRequestDTO));

        Hospital hospital = findById(updateRequestDTO.getId());

        List<Object[]> hospitals = hospitalRepository.validateHospitalDuplicityForUpdate(
                updateRequestDTO.getId(),
                updateRequestDTO.getName(),
                hospital.getEsewaMerchantCode(),
                hospital.getAlias()
        );

        validateDuplicity(hospitals,
                updateRequestDTO.getName(),
                hospital.getEsewaMerchantCode(),
                hospital.getAlias(),
                CLIENT);

        HmacApiInfo hmacApiInfo = hmacApiInfoRepository.getHmacApiInfoByHospitalId(updateRequestDTO.getId());

        save(parseToUpdatedHospital(updateRequestDTO, hospital));

        updateHospitalContactNumber(hospital.getId(), updateRequestDTO.getContactNumberUpdateRequestDTOS());

        if (updateRequestDTO.getIsLogoUpdate().equals(YES))
            updateHospitalLogo(hospital, logo);

        if (updateRequestDTO.getIsBannerUpdate().equals(YES))
            updateHospitalBanner(hospital, banner);

        updateHmacApiInfo(hmacApiInfo, updateRequestDTO.getStatus(), updateRequestDTO.getRemarks());

        if (updateRequestDTO.getBillingModeIds().size() > 0)
            updateBillingMode(updateRequestDTO, hospital);

        updateHospitalAppointmentServiceType(hospital, updateRequestDTO.getAppointmentServiceTypeUpdateRequestDTO());

        updateIsPrimaryHospitalAppointmentServiceTypeStatus(
                hospital.getId(),
                updateRequestDTO.getPrimaryAppointmentServiceTypeId()
        );

        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO hospitalSearchRequestDTO,
                                                   Pageable pageable) {
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

        HmacApiInfo hmacApiInfo = hmacApiInfoRepository.getHmacApiInfoByHospitalId(deleteRequestDTO.getId());

        save(parseToDeletedHospital(hospital, deleteRequestDTO));

        updateHmacApiInfo(hmacApiInfo, deleteRequestDTO.getStatus(), deleteRequestDTO.getRemarks());

        deleteHospitalBillingModeInfoList(deleteRequestDTO);

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
    public List<HospitalDropdownResponseDTO> fetchActiveHospitalForDropDown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, HOSPITAL);

        List<HospitalDropdownResponseDTO> responseDTOS = hospitalRepository.fetchActiveHospitalForDropDown();

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchHospitalForDropDown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, HOSPITAL);

        List<HospitalDropdownResponseDTO> responseDTOS = hospitalRepository.fetchHospitalForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL);

        HospitalResponseDTO responseDTO = hospitalRepository.fetchDetailsById(hospitalId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public String fetchAliasById(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL_ALIAS);

        String alias = hospitalRepository.fetchAliasById(hospitalId)
                .orElseThrow(() -> new NoContentFoundException(ALIAS_NOT_FOUND));

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL_ALIAS, getDifferenceBetweenTwoTime(startTime));

        return alias;
    }

    private void saveHospitalBillingModeInfo(Hospital hospital, List<Long> billingModeIds) {

        List<BillingMode> billingModes = new ArrayList<>();

        billingModeIds.forEach(billingModeId -> {
            billingModes.add(fetchBillingModeById(billingModeId));
        });

        saveHospitalBillingModeInfoList(parseToHospitalBillingModeInfoList.apply(hospital, billingModes));
    }

    private void deleteHospitalBillingModeInfo(Hospital hospital, List<Long> billingModeIds) {

        Long hospitalId = hospital.getId();

        List<HospitalBillingModeInfo> infoList = new ArrayList<>();

        billingModeIds.forEach(billingModeId -> {
            infoList.add(fetchHospitalBillingModeInfo(billingModeId, hospitalId));
        });

        saveHospitalBillingModeInfoList(deleteHospitalBillingModeInfoList.apply(infoList, hospital.getRemarks()));
    }

    private void deleteHospitalBillingModeInfoList(DeleteRequestDTO deleteRequestDTO) {

        List<HospitalBillingModeInfo> hospitalBillingModeInfos = fetchHospitalBillingModeInfo(deleteRequestDTO.getId());

        saveHospitalBillingModeInfoList(deleteHospitalBillingModeInfoList.apply(hospitalBillingModeInfos,
                deleteRequestDTO.getRemarks()));
    }

    private Hospital save(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    private void saveHmacApiInfo(HmacApiInfo hmacApiInfo) {
        hmacApiInfoRepository.save(hmacApiInfo);
    }

    private void saveHospitalBillingModeInfoList(List<HospitalBillingModeInfo> hospitalBillingModeInfo) {
        hospitalBillingModeInfoRepository.saveAll(hospitalBillingModeInfo);
    }

    private BillingMode fetchBillingModeById(Long id) {
        return billingModeRepository.fetchActiveBillingModeById(id)
                .orElseThrow(() -> BILLING_MODE_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private HospitalBillingModeInfo fetchHospitalBillingModeInfo(Long billingModeId, Long hospitalid) {
        return hospitalBillingModeInfoRepository.fetchHospitalBillingModeInfo(billingModeId, hospitalid);
    }

    private List<HospitalBillingModeInfo> fetchHospitalBillingModeInfo(Long hospitalid) {
        return hospitalBillingModeInfoRepository.fetchHospitalBillingModeInfoByHospitalId(hospitalid);
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

    private void saveHospitalLogo(Hospital hospital, MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{files});
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
//        String subDirectory = hospital.getClass().getSimpleName() + StringConstant.FORWARD_SLASH + hospital.getName();

        String subDirectory = hospital.getName();
        return minioFileService.addAttachmentIntoSubDirectory(subDirectory, files);
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

    private void updateHmacApiInfo(HmacApiInfo hmacApiInfo, Character status, String remarks) {
        HmacApiInfo hmacApiInfoToUpdate = updateHmacApiInfoAsHospital(
                hmacApiInfo,
                status,
                remarks);
        saveHmacApiInfo(hmacApiInfoToUpdate);
    }

    private void updateBillingMode(HospitalUpdateRequestDTO requestDTO, Hospital hospital) {
        List<HospitalBillingModeUpdateRequestDTO
                > billingModeIds = requestDTO.getBillingModeIds();

        List<Long> newBillingModeIds = new ArrayList<>();

        List<Long> deleteBillingModeIds = new ArrayList<>();

        billingModeIds.forEach(billingModeId -> {
            if (billingModeId.getStatus().equals(YES)) {
                newBillingModeIds.add(billingModeId.getBillingModeId());
            } else {
                deleteBillingModeIds.add(billingModeId.getBillingModeId());
            }
        });

        if (newBillingModeIds.size() > 0)
            saveHospitalBillingModeInfo(hospital, newBillingModeIds);

        if (deleteBillingModeIds.size() > 0)
            deleteHospitalBillingModeInfo(hospital, deleteBillingModeIds);

    }

    private void updateHospitalLogo(Hospital hospital, MultipartFile files) {
        HospitalLogo hospitalLogo = hospitalLogoRepository.findHospitalLogoByHospitalId(hospital.getId());

        if (Objects.isNull(hospitalLogo)) saveHospitalLogo(hospital, files);
        else updateHospitalLogo(hospital, hospitalLogo, files);
    }

    private void updateHospitalLogo(Hospital hospital, HospitalLogo hospitalLogo, MultipartFile files) {

        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{files});
            setLogoFileProperties(responseList.get(0), hospitalLogo);
        } else
            hospitalLogo.setStatus(INACTIVE);
    }

    private void updateHospitalBanner(Hospital hospital, HospitalBanner hospitalBanner, MultipartFile banner) {

        if (!Objects.isNull(banner)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(hospital, new MultipartFile[]{banner});
            setBannerFileProperties(responseList.get(0), hospitalBanner);
        } else
            hospitalBanner.setStatus(INACTIVE);
    }

    private void updateHospitalBanner(Hospital hospital, MultipartFile banner) {
        HospitalBanner hospitalBanner = hospitalBannerRepository.findHospitalBannerByHospitalId(hospital.getId());

        if (Objects.isNull(hospitalBanner)) saveHospitalBanner(hospital, banner);
        else updateHospitalBanner(hospital, hospitalBanner, banner);
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND, CLIENT), "id", id.toString());
    };

    private Function<Long, NoContentFoundException> BILLING_MODE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, BILLING_MODE, id);
        throw new NoContentFoundException(BillingMode.class, "id", id.toString());
    };

    private void saveHospitalAppointmentServiceType(Hospital hospital, List<Long> appointmentServiceTypeIds,
                                                    Long primaryAppointmentServiceTypeId) {

        List<HospitalAppointmentServiceType> hospitalAppointmentServiceTypes =
                appointmentServiceTypeIds.stream()
                        .map(appointmentServiceTypeId -> {

                            AppointmentServiceType appointmentServiceType =
                                    appointmentServiceTypeService.fetchActiveById(appointmentServiceTypeId);

                            Character isPrimary = appointmentServiceTypeId.equals(primaryAppointmentServiceTypeId)
                                    ? YES : NO;

                            return parseToHospitalAppointmentServiceType(hospital, appointmentServiceType, isPrimary);
                        }).collect(Collectors.toList());

        saveHospitalAppointmentServiceType(hospitalAppointmentServiceTypes);
    }

    /*WHILE UPDATING, IS PRIMARY IS BY DEFAULT SET AS 'N' AND UPDATE QUERY IS EXECUTED TO CHANGE THE STATUS*/
    private void updateHospitalAppointmentServiceType(Hospital hospital,
                                                      List<HospitalAppointmentServiceTypeUpdateRequestDTO> updateRequestDTOS) {

        List<HospitalAppointmentServiceType> hospitalAppointmentServiceTypes =
                updateRequestDTOS.stream()
                        .map(updateRequestDTO -> {

                            if (Objects.isNull(updateRequestDTO.getHospitalAppointmentServiceTypeId())) {

                                AppointmentServiceType appointmentServiceType =
                                        appointmentServiceTypeService.fetchActiveById(
                                                updateRequestDTO.getAppointmentServiceTypeId());

                                return parseToHospitalAppointmentServiceType(hospital, appointmentServiceType, NO);

                            } else {
                                HospitalAppointmentServiceType hospitalAppointmentServiceType =
                                        findHospitalAppointmentServiceTypeById(
                                                updateRequestDTO.getHospitalAppointmentServiceTypeId());

                                return updateHospitalAppointmentServiceTypeStatus(hospitalAppointmentServiceType,
                                        updateRequestDTO.getStatus(), NO);
                            }
                        }).collect(Collectors.toList());

        saveHospitalAppointmentServiceType(hospitalAppointmentServiceTypes);
    }

    private void saveHospitalAppointmentServiceType(List<HospitalAppointmentServiceType> hospitalAppointmentServiceTypes) {
        hospitalAppointmentServiceTypeRepository.saveAll(hospitalAppointmentServiceTypes);
    }

    private HospitalAppointmentServiceType findHospitalAppointmentServiceTypeById(Long hospitalAppointmentServiceType) {
        return hospitalAppointmentServiceTypeRepository.fetchActiveById(hospitalAppointmentServiceType)
                .orElseThrow(() -> new NoContentFoundException(HospitalAppointmentServiceType.class,
                        "hospitalAppointmentServiceType", hospitalAppointmentServiceType.toString()));
    }

    private void updateIsPrimaryHospitalAppointmentServiceTypeStatus(Long hospitalId, Long appointmentServiceTypeId) {
        hospitalAppointmentServiceTypeRepository.updateIsPrimaryStatus(hospitalId, appointmentServiceTypeId);
    }


}
