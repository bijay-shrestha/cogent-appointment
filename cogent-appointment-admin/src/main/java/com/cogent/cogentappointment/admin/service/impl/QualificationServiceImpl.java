package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.constants.QualificationLog;
import com.cogent.cogentappointment.admin.model.Country;
import com.cogent.cogentappointment.admin.model.Qualification;
import com.cogent.cogentappointment.admin.model.QualificationAlias;
import com.cogent.cogentappointment.admin.repository.QualificationRepository;
import com.cogent.cogentappointment.admin.service.CountryService;
import com.cogent.cogentappointment.admin.service.QualificationAliasService;
import com.cogent.cogentappointment.admin.service.QualificationService;
import com.cogent.cogentappointment.admin.utils.QualificationUtils;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.log.CommonLogConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author smriti on 11/11/2019
 */
@Service
@Transactional
@Slf4j
public class QualificationServiceImpl implements QualificationService {

    private final QualificationRepository qualificationRepository;

    private final CountryService countryService;

    private final QualificationAliasService qualificationAliasService;

    public QualificationServiceImpl(QualificationRepository qualificationRepository,
                                    CountryService countryService,
                                    QualificationAliasService qualificationAliasService) {
        this.qualificationRepository = qualificationRepository;
        this.countryService = countryService;
        this.qualificationAliasService = qualificationAliasService;
    }

    @Override
    public void save(QualificationRequestDTO requestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, QualificationLog.QUALIFICATION);

        Country country = fetchCountry(requestDTO.getCountryId());

        QualificationAlias qualificationAlias = fetchQualificationAlias(requestDTO.getQualificationAliasId());

        save(QualificationUtils.parseToQualification(requestDTO, country, qualificationAlias));

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, QualificationLog.QUALIFICATION, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(QualificationUpdateRequestDTO requestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, QualificationLog.QUALIFICATION);

        Qualification qualification = findQualificationById(requestDTO.getId());

        Country country = fetchCountry(requestDTO.getCountryId());

        QualificationAlias qualificationAlias = fetchQualificationAlias(requestDTO.getQualificationAliasId());

        QualificationUtils.parseToUpdatedQualification(requestDTO, country, qualificationAlias, qualification);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, QualificationLog.QUALIFICATION, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.DELETING_PROCESS_STARTED, QualificationLog.QUALIFICATION);

        Qualification qualification = findQualificationById(deleteRequestDTO.getId());

        QualificationUtils.parseToDeletedQualification(qualification, deleteRequestDTO);

        log.info(CommonLogConstant.DELETING_PROCESS_COMPLETED, QualificationLog.QUALIFICATION, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                        Pageable pageable) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SEARCHING_PROCESS_STARTED, QualificationLog.QUALIFICATION);

        List<QualificationMinimalResponseDTO> responseDTOS =
                qualificationRepository.search(searchRequestDTO, pageable);

        log.info(CommonLogConstant.SEARCHING_PROCESS_COMPLETED, QualificationLog.QUALIFICATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public QualificationResponseDTO fetchDetailsById(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, QualificationLog.QUALIFICATION);

        QualificationResponseDTO responseDTO = qualificationRepository.fetchDetailsById(id);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, QualificationLog.QUALIFICATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<QualificationDropdownDTO> fetchActiveQualificationForDropDown() {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, QualificationLog.QUALIFICATION);

        List<QualificationDropdownDTO> responseDTOS = qualificationRepository.fetchActiveQualificationForDropDown();

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, QualificationLog.QUALIFICATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Qualification fetchQualificationById(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED, QualificationLog.QUALIFICATION);

        Qualification qualification = qualificationRepository.fetchActiveQualificationById(id)
                .orElseThrow(() -> new NoContentFoundException(Qualification.class, "id", id.toString()));

        log.info(CommonLogConstant.FETCHING_PROCESS_COMPLETED, QualificationLog.QUALIFICATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return qualification;
    }

    private Country fetchCountry(Long id) {
        return countryService.fetchCountryById(id);
    }

    private QualificationAlias fetchQualificationAlias(Long id) {
        return qualificationAliasService.fetchQualificationAliasById(id);
    }

    private Qualification findQualificationById(Long id) {
        return qualificationRepository.findQualificationById(id)
                .orElseThrow(() -> new NoContentFoundException(Qualification.class, "id", id.toString()));
    }

    private void save(Qualification qualification) {
        qualificationRepository.save(qualification);
    }
}
