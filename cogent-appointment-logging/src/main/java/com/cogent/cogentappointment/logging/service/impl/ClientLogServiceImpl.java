package com.cogent.cogentappointment.logging.service.impl;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import com.cogent.cogentappointment.logging.repository.AdminRepository;
import com.cogent.cogentappointment.logging.repository.ClientLogRepository;
import com.cogent.cogentappointment.logging.service.ClientLogService;
import com.cogent.cogentappointment.logging.utils.common.SecurityContextUtils;
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
    private final AdminRepository adminRepository;

    public ClientLogServiceImpl(ClientLogRepository clientLogRepository, AdminRepository adminRepository) {
        this.clientLogRepository = clientLogRepository;
        this.adminRepository = adminRepository;
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

        UserMenuStaticsResponseDTO responseDTOS = clientLogRepository.fetchUserMenuLogsStaticsByClientId(searchRequestDTO, pageable);

        log.info(USER_MENU_LOG_STATICS_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        String email = SecurityContextUtils.getLoggedInAdminEmail();
        Long hospitalId = adminRepository.findAdminByEmail(email).get().getId();
        UserMenuLogResponseDTO responseDTOS = clientLogRepository.search(searchRequestDTO, hospitalId, pageable);

        log.info(USER_MENU_LOG_SEARCH_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_STATICS_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        String email = SecurityContextUtils.getLoggedInAdminEmail();
        Long hospitalId = adminRepository.findAdminByEmail(email).get().getId();

        UserMenuStaticsResponseDTO responseDTOS = clientLogRepository.fetchUserMenuLogsStatics(searchRequestDTO, hospitalId, pageable);

        log.info(USER_MENU_LOG_STATICS_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsforDiagram(ClientLogSearchRequestDTO searchRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_STATICS_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        String email = SecurityContextUtils.getLoggedInAdminEmail();
        Long hospitalId = adminRepository.findAdminByEmail(email).get().getId();

        UserMenuStaticsResponseDTO responseDTOS = clientLogRepository.fetchUserMenuLogsStaticsforDiagram(searchRequestDTO, hospitalId);

        log.info(USER_MENU_LOG_STATICS_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsforDiagramByClientId(ClientLogSearchRequestDTO searchRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_MENU_LOG_STATICS_SEARCH_PROCESS_STARTED, USER_MENU_LOG);

        UserMenuStaticsResponseDTO responseDTOS = clientLogRepository.fetchUserMenuLogsStaticsforDiagramByClientId(searchRequestDTO);

        log.info(USER_MENU_LOG_STATICS_PROCESS_COMPLETED, USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

}
