package com.cogent.cogentappointment.logging.service.impl;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogStaticsResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import com.cogent.cogentappointment.logging.repository.AdminLogRepository;
import com.cogent.cogentappointment.logging.service.AdminLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.cogent.cogentappointment.logging.log.UserLog.*;
import static com.cogent.cogentappointment.logging.utils.common.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.logging.utils.common.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Rupak
 */
@Service
@Transactional
@Slf4j
public class AdminLogServiceImpl implements AdminLogService {

    private final AdminLogRepository adminLogRepository;

    public AdminLogServiceImpl(AdminLogRepository adminLogRepository) {
        this.adminLogRepository = adminLogRepository;
    }

    @Override
    public AdminLogResponseDTO search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_LOG_SEARCH_PROCESS_STARTED, USER_LOG);

        AdminLogResponseDTO responseDTOS = adminLogRepository.search(searchRequestDTO, pageable);

        log.info(USER_LOG_SEARCH_PROCESS_COMPLETED, USER_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(AdminLogSearchRequestDTO searchRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(USER_LOG_SEARCH_PROCESS_STARTED, USER_LOG);

        UserMenuStaticsResponseDTO responseDTOS = adminLogRepository.fetchUserMenuLogsStatics(searchRequestDTO);

        log.info(USER_LOG_SEARCH_PROCESS_COMPLETED, USER_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }
}
