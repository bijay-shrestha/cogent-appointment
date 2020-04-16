package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.client.repository.AdminLogRepository;
import com.cogent.cogentappointment.client.repository.AdminRepository;
import com.cogent.cogentappointment.client.service.AdminLogService;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.admin.log.constants.AdminLog.ADMIN_USER_MENU_LOG;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.utils.AdminLogUtils.parseToAdminLog;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Rupak
 */
@Service
@Transactional
@Slf4j
public class AdminLogServiceImpl implements AdminLogService {

    private final AdminLogRepository adminLogRepository;
    private final AdminRepository adminRepository;

    public AdminLogServiceImpl(AdminLogRepository adminLogRepository, AdminRepository adminRepository) {
        this.adminLogRepository = adminLogRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public void save(AdminLogRequestDTO requestDTO, Character status, String ipAddress) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN_USER_MENU_LOG);

        Admin admin = adminRepository.findAdminById(requestDTO.getAdminId()).get();
        AdminLog adminLog = parseToAdminLog(requestDTO, status, admin, ipAddress);
        adminLogRepository.save(adminLog);

        log.info(SAVING_PROCESS_COMPLETED, ADMIN_USER_MENU_LOG, getDifferenceBetweenTwoTime(startTime));

    }


}
