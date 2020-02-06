package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.QualificationAliasRepository;
import com.cogent.cogentappointment.admin.service.QualificationAliasService;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.QualificationAliasLog.QUALIFICATION_ALIAS;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

        QualificationAlias qualificationAlias = qualificationAliasRepository.fetchActiveQualificationAliasById(id)
                .orElseThrow(() -> new NoContentFoundException(QualificationAlias.class, "id", id.toString()));

        log.info(FETCHING_PROCESS_COMPLETED, QUALIFICATION_ALIAS, getDifferenceBetweenTwoTime(startTime));

        return qualificationAlias;
    }
}
