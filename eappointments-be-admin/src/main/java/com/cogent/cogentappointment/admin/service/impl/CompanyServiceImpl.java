package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.admin.repository.HospitalContactNumberRepository;
import com.cogent.cogentappointment.admin.repository.HospitalLogoRepository;
import com.cogent.cogentappointment.admin.repository.HospitalRepository;
import com.cogent.cogentappointment.admin.service.CompanyService;
import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalContactNumber;
import com.cogent.cogentappointment.persistence.model.HospitalLogo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.Validator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.ALIAS_NOT_FOUND;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.CompanyLog.COMPANY;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.admin.utils.CompanyUtils.*;
import static com.cogent.cogentappointment.admin.utils.HmacApiInfoUtils.parseToHmacApiInfo;
import static com.cogent.cogentappointment.admin.utils.HmacApiInfoUtils.updateHmacApiInfoAsHospital;
import static com.cogent.cogentappointment.admin.utils.HospitalUtils.convertFileToHospitalLogo;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.NameAndCodeValidationUtils.validateDuplicity;

@Service
@Transactional
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final HospitalRepository hospitalRepository;

    private final HospitalContactNumberRepository hospitalContactNumberRepository;

    private final HospitalLogoRepository hospitalLogoRepository;

    private final HmacApiInfoRepository hmacApiInfoRepository;

    private final Validator validator;

    public CompanyServiceImpl(HospitalRepository hospitalRepository,
                              HospitalContactNumberRepository hospitalContactNumberRepository,
                              HospitalLogoRepository hospitalLogoRepository,
                              HmacApiInfoRepository hmacApiInfoRepository,
                              Validator validator) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalContactNumberRepository = hospitalContactNumberRepository;
        this.hospitalLogoRepository = hospitalLogoRepository;
        this.hmacApiInfoRepository = hmacApiInfoRepository;
        this.validator = validator;
    }


    @Override
    public void save(@Valid CompanyRequestDTO requestDTO) throws NoSuchAlgorithmException {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, COMPANY);

        validateConstraintViolation(validator.validate(requestDTO));

        List<Object[]> companies = hospitalRepository.validateCompanyDuplicity(
                requestDTO.getName(), requestDTO.getCompanyCode());

        validateDuplicity(companies, requestDTO.getName(), requestDTO.getCompanyCode(),
                "COMPANY");

        Hospital company = save(convertCompanyDTOToHospital(requestDTO));

        saveCompanyContactNumber(company.getId(), requestDTO.getContactNumber());

        saveCompanyLogo(company, requestDTO.getCompanyLogo());

        saveHmacApiInfo(parseToHmacApiInfo(company));

        log.info(SAVING_PROCESS_COMPLETED, COMPANY, getDifferenceBetweenTwoTime(startTime));

    }

    @Override
    public void update(@Valid CompanyUpdateRequestDTO updateRequestDTO) throws NoSuchAlgorithmException {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, COMPANY);

        validateConstraintViolation(validator.validate(updateRequestDTO));

        Hospital company = findById(updateRequestDTO.getId());

        List<Object[]> hospitals = hospitalRepository.validateCompanyDuplicityForUpdate(
                updateRequestDTO.getId(), updateRequestDTO.getName(), updateRequestDTO.getCompanyCode());

        validateDuplicity(hospitals, updateRequestDTO.getName(),
                updateRequestDTO.getCompanyCode(), "COMPANY");

        HmacApiInfo hmacApiInfo = hmacApiInfoRepository.getHmacApiInfoByCompanyId(updateRequestDTO.getId());

        save(parseToUpdatedCompany(updateRequestDTO, company));

        updateCompanyContactNumber(company.getId(), updateRequestDTO.getContactNumberUpdateRequestDTOS());

        if (updateRequestDTO.getIsLogoUpdate().equals(YES))
            updateCompanyLogo(company, updateRequestDTO.getCompanyLogo());

        updateHmacApiInfo(hmacApiInfo, updateRequestDTO.getStatus(), updateRequestDTO.getRemarks());

        log.info(UPDATING_PROCESS_COMPLETED, COMPANY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, COMPANY);

        Hospital company = findById(deleteRequestDTO.getId());

        HmacApiInfo hmacApiInfo = hmacApiInfoRepository.getHmacApiInfoByCompanyId(deleteRequestDTO.getId());

        save(parseToDeletedCompany(company, deleteRequestDTO));

        updateHmacApiInfo(hmacApiInfo, deleteRequestDTO.getStatus(), deleteRequestDTO.getRemarks());

        log.info(DELETING_PROCESS_COMPLETED, COMPANY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<CompanyMinimalResponseDTO> search(CompanySearchRequestDTO searchRequestDTO,
                                                  Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, COMPANY);

        List<CompanyMinimalResponseDTO> responseDTOS = hospitalRepository.searchCompany(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, COMPANY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<CompanyDropdownResponseDTO> fetchCompanyForDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, COMPANY);

        List<CompanyDropdownResponseDTO> responseDTOS = hospitalRepository.fetchCompanyForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, COMPANY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<CompanyDropdownResponseDTO> fetchActiveCompanyForDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, COMPANY);

        List<CompanyDropdownResponseDTO> responseDTOS = hospitalRepository.fetchActiveCompanyForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, COMPANY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public CompanyResponseDTO fetchDetailsById(Long companyId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, COMPANY);

        CompanyResponseDTO responseDTO = hospitalRepository.fetchCompanyDetailsById(companyId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, COMPANY, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public String fetchAliasById(Long companyId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_ALIAS_PROCESS_STARTED, HOSPITAL);

        String responseDTO = hospitalRepository.fetchAliasById(companyId)
                .orElseThrow(() -> new NoContentFoundException(ALIAS_NOT_FOUND));

        log.info(FETCHING_ALIAS_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public Hospital findActiveCompanyById(Long companyById) {
        return hospitalRepository.findActiveCompanyById(companyById)
                .orElseThrow(() -> COMPANY_WITH_GIVEN_ID_NOT_FOUND.apply(companyById));
    }

    private Hospital save(Hospital company) {
        return hospitalRepository.save(company);
    }

    private void saveHmacApiInfo(HmacApiInfo hmacApiInfo) {
        hmacApiInfoRepository.save(hmacApiInfo);
    }

    private void saveCompanyContactNumber(Long CompanyId, List<String> contactNumbers) {
        List<HospitalContactNumber> hospitalContactNumbers = contactNumbers.stream()
                .map(contactNumber -> parseToCompanyContactNumber(CompanyId, contactNumber))
                .collect(Collectors.toList());

        saveCompanyContactNumber(hospitalContactNumbers);
    }

    private void saveCompanyContactNumber(List<HospitalContactNumber> companyContactNumbers) {
        hospitalContactNumberRepository.saveAll(companyContactNumbers);
    }

    private void saveCompanyLogo(Hospital hospital, String companyLogo) {
        if (!Objects.isNull(companyLogo))
            saveCompanyLogo(convertFileToHospitalLogo(new HospitalLogo(), companyLogo, hospital));
    }

    private void saveCompanyLogo(HospitalLogo companyLogo) {
        hospitalLogoRepository.save(companyLogo);
    }

    private Hospital findById(Long id) {
        return hospitalRepository.findCompanyById(id).orElseThrow(() -> COMPANY_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private void updateCompanyContactNumber(Long companyId,
                                            List<CompanyContactNumberUpdateRequestDTO> updateRequestDTOS) {

        List<HospitalContactNumber> hospitalContactNumbers = updateRequestDTOS.stream()
                .map(requestDTO -> parseToUpdatedCompanyContactNumber(companyId, requestDTO))
                .collect(Collectors.toList());

        saveCompanyContactNumber(hospitalContactNumbers);
    }

    private void updateHmacApiInfo(HmacApiInfo hmacApiInfo, Character status, String remarks) {
        HmacApiInfo hmacApiInfoToUpdate = updateHmacApiInfoAsHospital(
                hmacApiInfo,
                status,
                remarks);
        saveHmacApiInfo(hmacApiInfoToUpdate);
    }

    private void updateCompanyLogo(Hospital hospital, String companyLogoImage) {
        HospitalLogo hospitalLogo = hospitalLogoRepository.findHospitalLogoByHospitalId(hospital.getId());

        if (Objects.isNull(hospitalLogo)) saveCompanyLogo(hospital, companyLogoImage);
        else updateHospitalLogo(hospital, hospitalLogo, companyLogoImage);
    }

    private void updateHospitalLogo(Hospital company, HospitalLogo companyLogo, String companyLogoImage) {

        if (!Objects.isNull(companyLogoImage)) {
            convertFileToHospitalLogo(companyLogo, companyLogoImage, company);
        } else companyLogo.setStatus(INACTIVE);

        saveCompanyLogo(companyLogo);
    }

    private Function<Long, NoContentFoundException> COMPANY_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, COMPANY, id);
        throw new NoContentFoundException("Company Doesn't Exists");
    };
}
