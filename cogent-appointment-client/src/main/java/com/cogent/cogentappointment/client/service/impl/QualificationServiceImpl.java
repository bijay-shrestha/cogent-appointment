package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualification.QualificationUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.qualification.QualificationResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.QualificationRepository;
import com.cogent.cogentappointment.client.service.HospitalService;
import com.cogent.cogentappointment.client.service.QualificationAliasService;
import com.cogent.cogentappointment.client.service.QualificationService;
import com.cogent.cogentappointment.client.service.UniversityService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Qualification;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
import com.cogent.cogentappointment.persistence.model.University;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.QualificationLog.QUALIFICATION;
import static com.cogent.cogentappointment.client.utils.QualificationUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author smriti on 11/11/2019
 */
@Service
@Transactional
@Slf4j
public class QualificationServiceImpl implements QualificationService {

    private final QualificationRepository qualificationRepository;

    private final QualificationAliasService qualificationAliasService;

    private final UniversityService universityService;

    private final HospitalService hospitalService;

    public QualificationServiceImpl(QualificationRepository qualificationRepository,
                                    QualificationAliasService qualificationAliasService,
                                    UniversityService universityService,
                                    HospitalService hospitalService) {
        this.qualificationRepository = qualificationRepository;
        this.qualificationAliasService = qualificationAliasService;
        this.universityService = universityService;
        this.hospitalService = hospitalService;
    }

    @Override
    public void save(QualificationRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, QUALIFICATION);

        Long hospitalId = getLoggedInHospitalId();

        QualificationAlias qualificationAlias = fetchQualificationAlias(requestDTO.getQualificationAliasId());

        University university = fetchUniversity(requestDTO.getUniversityId());

        Hospital hospital = fetchHospital(hospitalId);

        Long count = qualificationRepository.validateDuplicity(
                requestDTO.getName(),
                hospitalId
        );

        validateName(count, requestDTO.getName());

        save(parseToQualification(requestDTO, university, qualificationAlias, hospital));

        log.info(SAVING_PROCESS_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(QualificationUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, QUALIFICATION);

        Qualification qualification = findQualificationById(requestDTO.getId());

        Long hospitalId = getLoggedInHospitalId();

        Long count = qualificationRepository.validateDuplicity(
                requestDTO.getId(),
                requestDTO.getName(),
                hospitalId);

        validateName(count, requestDTO.getName());

        QualificationAlias qualificationAlias = fetchQualificationAlias(requestDTO.getQualificationAliasId());

        University university = fetchUniversity(requestDTO.getUniversityId());

        Hospital hospital = fetchHospital(hospitalId);

        parseToUpdatedQualification(requestDTO, qualificationAlias, university, qualification, hospital);

        log.info(UPDATING_PROCESS_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, QUALIFICATION);

        Qualification qualification = findQualificationById(deleteRequestDTO.getId());

        parseToDeletedQualification(qualification, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                        Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, QUALIFICATION);

        List<QualificationMinimalResponseDTO> responseDTOS =
                qualificationRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public QualificationResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, QUALIFICATION);

        QualificationResponseDTO responseDTO = qualificationRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<QualificationDropdownDTO> fetchActiveQualificationForDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, QUALIFICATION);

        List<QualificationDropdownDTO> responseDTOS = qualificationRepository.fetchActiveQualificationForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Qualification fetchQualificationById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, QUALIFICATION);

        Qualification qualification = qualificationRepository.fetchActiveQualificationById(id)
                .orElseThrow(() -> new NoContentFoundException(Qualification.class, "id", id.toString()));

        log.info(FETCHING_PROCESS_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));

        return qualification;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinQualification() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, QUALIFICATION);

        List<DropDownResponseDTO> responseDTOS = qualificationRepository.fetchMinQualification();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, QUALIFICATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private void validateName(Long qualificationCount, String name) {
        if (qualificationCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, Qualification.class.getSimpleName(), name));
    }

    private QualificationAlias fetchQualificationAlias(Long id) {
        return qualificationAliasService.fetchQualificationAliasById(id);
    }

    private Qualification findQualificationById(Long id) {
        return qualificationRepository.findQualificationById(id)
                .orElseThrow(() -> new NoContentFoundException(Qualification.class, "id", id.toString()));
    }

    private University fetchUniversity(Long universityId) {
        return universityService.fetchUniversityById(universityId);
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private void save(Qualification qualification) {
        qualificationRepository.save(qualification);
    }
}
