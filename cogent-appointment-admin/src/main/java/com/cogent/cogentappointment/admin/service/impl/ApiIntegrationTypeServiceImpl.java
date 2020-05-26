package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.repository.ApiIntegrationTypeRepository;
import com.cogent.cogentappointment.admin.service.ApiIntegrationTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_INTEGRATION_TYPE;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak on 2020-05-26
 */
@Service
@Transactional
@Slf4j
public class ApiIntegrationTypeServiceImpl implements ApiIntegrationTypeService {

    private final ApiIntegrationTypeRepository apiIntegrationTypeRepository;

    public ApiIntegrationTypeServiceImpl(ApiIntegrationTypeRepository apiIntegrationTypeRepository) {
        this.apiIntegrationTypeRepository = apiIntegrationTypeRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveApiIntegrationType() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, API_INTEGRATION_TYPE);

        List<DropDownResponseDTO> downResponseDTOList = apiIntegrationTypeRepository.fetchActiveApiIntegrationType();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, API_INTEGRATION_TYPE, getDifferenceBetweenTwoTime(startTime));

        return downResponseDTOList;
    }
}
