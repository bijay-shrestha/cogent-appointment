package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.repository.IntegrationFeatureRepository;
import com.cogent.cogentappointment.admin.service.IntegrationFeatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.FEATURES;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak on 2020-05-19
 */
@Service
@Transactional
@Slf4j
public class IntegrationFeatureServiceImpl implements IntegrationFeatureService {

    private final IntegrationFeatureRepository integrationFeatureRepository;

    public IntegrationFeatureServiceImpl(IntegrationFeatureRepository integrationFeatureRepository) {
        this.integrationFeatureRepository = integrationFeatureRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveFeatureType() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, FEATURES);

        List<DropDownResponseDTO> responseDTOS = integrationFeatureRepository.fetchActiveFeatureType();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, FEATURES, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }
}
