package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AdminRepository;
import com.cogent.cogentappointment.client.repository.ClientLogRepository;
import com.cogent.cogentappointment.client.service.ClientLogService;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.ClientLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.ADMIN_USER_MENU_LOG;
import static com.cogent.cogentappointment.client.utils.ClientLogUtils.parseToClientLog;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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
    public void save(ClientLogRequestDTO requestDTO, Character status) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN_USER_MENU_LOG);

        Admin admin = adminRepository.findAdminById(requestDTO.getAdminId())
                .orElseThrow(() -> new NoContentFoundException(Admin.class));

        ClientLog clientLog = parseToClientLog(requestDTO, status, admin);
        clientLogRepository.save(clientLog);

        log.info(SAVING_PROCESS_COMPLETED, ADMIN_USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

    }


}
