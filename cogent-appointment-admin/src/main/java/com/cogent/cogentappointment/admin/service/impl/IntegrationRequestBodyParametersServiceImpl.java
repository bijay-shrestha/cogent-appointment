package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.repository.IntegrationRequestBodyParametersRepository;
import com.cogent.cogentappointment.admin.service.IntegrationRequestBodyParametersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_REQUEST_BODY_PARAMETERS;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.FEATURES;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/05/29-10:16 AM
 */
@Service
@Transactional
@Slf4j
public class IntegrationRequestBodyParametersServiceImpl implements IntegrationRequestBodyParametersService {

    private final IntegrationRequestBodyParametersRepository requestBodyParametersRepository;

    public IntegrationRequestBodyParametersServiceImpl(IntegrationRequestBodyParametersRepository requestBodyParametersRepository) {
        this.requestBodyParametersRepository = requestBodyParametersRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveRequestBodyParameters() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, API_REQUEST_BODY_PARAMETERS);

        List<DropDownResponseDTO> responseDTOS = requestBodyParametersRepository.fetchActiveRequestBodyParameters();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, API_REQUEST_BODY_PARAMETERS, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }
}
