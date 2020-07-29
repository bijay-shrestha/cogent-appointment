package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.repository.HttpRequestMethodRepository;
import com.cogent.cogentappointment.admin.service.HttpRequestMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.HTTP_REQUEST_METHODS;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak on 2020-05-19
 */
@Service
@Transactional
@Slf4j
public class HttpRequestMethodServiceImpl implements HttpRequestMethodService {

    private final HttpRequestMethodRepository httpRequestMethodRepository;

    public HttpRequestMethodServiceImpl(HttpRequestMethodRepository httpRequestMethodRepository) {
        this.httpRequestMethodRepository = httpRequestMethodRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveRequestMethod() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, HTTP_REQUEST_METHODS);

        List<DropDownResponseDTO> responseDTOS = httpRequestMethodRepository.fetchActiveRequestMethod();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, HTTP_REQUEST_METHODS, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

}
