package com.cogent.cogentappointment.logging.service.impl;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import com.cogent.cogentappointment.logging.repository.ClientLogRepository;
import com.cogent.cogentappointment.logging.service.ClientLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.cogent.cogentappointment.logging.log.UserMenuLog.*;
import static com.cogent.cogentappointment.logging.utils.common.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.logging.utils.common.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Rupak
 */
@Service
@Transactional
@Slf4j
public class ClientLogServiceImpl implements ClientLogService {

    private final ClientLogRepository clientLogRepository;

    public ClientLogServiceImpl(ClientLogRepository clientLogRepository) {
        this.clientLogRepository = clientLogRepository;
    }

    @Override
    public UserMenuLogResponseDTO searchByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        UserMenuLogResponseDTO responseDTOS = clientLogRepository.searchByClientId(searchRequestDTO, pageable);

        log.info(USER_MENU_LOG_SEARCH_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_STATICS_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        UserMenuStaticsResponseDTO responseDTOS = clientLogRepository.fetchUserMenuLogsStatics(searchRequestDTO, pageable);

        log.info(USER_MENU_LOG_STATICS_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        UserMenuLogResponseDTO responseDTOS = clientLogRepository.search(searchRequestDTO, pageable);

        log.info(USER_MENU_LOG_SEARCH_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_STATICS_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        UserMenuStaticsResponseDTO responseDTOS = clientLogRepository.fetchUserMenuLogsStatics(searchRequestDTO, pageable);

        log.info(USER_MENU_LOG_STATICS_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

}
