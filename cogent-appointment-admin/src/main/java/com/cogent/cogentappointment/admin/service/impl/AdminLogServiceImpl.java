package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.repository.AdminLogRepository;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import com.cogent.cogentappointment.persistence.model.AdminLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.cogent.cogentappointment.admin.utils.AdminLogUtils.parseToAdminLog;

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
    public void save(AdminLogRequestDTO requestDTO, HttpServletRequest request) {

        AdminLog adminLog = parseToAdminLog(requestDTO, request.getRemoteAddr());
        adminLogRepository.save(adminLog);

    }


}
