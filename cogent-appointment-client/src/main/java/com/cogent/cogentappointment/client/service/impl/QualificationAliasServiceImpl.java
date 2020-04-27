package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.qualificationAlias.QualificationAliasMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.QualificationAliasRepository;
import com.cogent.cogentappointment.client.service.QualificationAliasService;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.QualificationAliasLog.QUALIFICATION_ALIAS;
import static com.cogent.cogentappointment.client.utils.QualificationAliasUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 11/11/2019
 */
@Service
@Transactional
@Slf4j
public class QualificationAliasServiceImpl implements QualificationAliasService {
    private final QualificationAliasRepository qualificationAliasRepository;

    public QualificationAliasServiceImpl(QualificationAliasRepository qualificationAliasRepository) {
        this.qualificationAliasRepository = qualificationAliasRepository;
    }

    @Override
    public void save(QualificationAliasRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, QUALIFICATION_ALIAS);

        Long count = qualificationAliasRepository.validateDuplicity(requestDTO.getName());

        validateName(count, requestDTO.getName());

        save(parseToQualificationAlias(requestDTO));

        log.info(SAVING_PROCESS_COMPLETED, QUALIFICATION_ALIAS, getDifferenceBetweenTwoTime(startTime));

    }

    @Override
    public void update(QualificationAliasUpdateRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, QUALIFICATION_ALIAS);

        QualificationAlias qualificationAliasToBeUpdated = fetchQualificationAliasById(requestDTO.getId());

        Long count = qualificationAliasRepository.validateDuplicity(requestDTO.getId(), requestDTO.getName());

        validateName(count, requestDTO.getName());

        parseToUpdatedQualificationAlias(qualificationAliasToBeUpdated, requestDTO);

        log.info(UPDATING_PROCESS_COMPLETED, QUALIFICATION_ALIAS, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, QUALIFICATION_ALIAS);

        QualificationAlias qualification = fetchQualificationAliasById(deleteRequestDTO.getId());

        parseToDeletedQualification(qualification, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, QUALIFICATION_ALIAS, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveQualificationAlias() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, QUALIFICATION_ALIAS);

        List<DropDownResponseDTO> responseDTOS = qualificationAliasRepository.fetchActiveQualificationAlias();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, QUALIFICATION_ALIAS, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public QualificationAlias fetchQualificationAliasById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, QUALIFICATION_ALIAS);

        QualificationAlias qualificationAlias = qualificationAliasRepository.fetchQualificationAliasById(id)
                .orElseThrow(() -> QUALIFICATION_ALIAS_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, QUALIFICATION_ALIAS, getDifferenceBetweenTwoTime(startTime));

        return qualificationAlias;
    }

    @Override
    public List<QualificationAliasMinimalResponseDTO> search(QualificationAliasSearchRequestDTO searchRequestDTO,
                                                             Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, QUALIFICATION_ALIAS);

        List<QualificationAliasMinimalResponseDTO> responseDTOS =
                qualificationAliasRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, QUALIFICATION_ALIAS, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private void validateName(Long qualificationCount, String name) {
        if (qualificationCount.intValue() > 0) {
            log.error(NAME_DUPLICATION_ERROR, QUALIFICATION_ALIAS, name);
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, QualificationAlias.class.getSimpleName(), name));
        }
    }

    private void save(QualificationAlias qualificationAlias) {
        qualificationAliasRepository.save(qualificationAlias);
    }

    private Function<Long, NoContentFoundException> QUALIFICATION_ALIAS_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, QUALIFICATION_ALIAS, id);
        throw new NoContentFoundException(QualificationAlias.class, "id", id.toString());
    };
}
