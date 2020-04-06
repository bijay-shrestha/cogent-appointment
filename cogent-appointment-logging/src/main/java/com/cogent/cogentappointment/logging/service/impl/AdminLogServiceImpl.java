package com.cogent.cogentappointment.logging.service.impl;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.repository.AdminLogRepository;
import com.cogent.cogentappointment.logging.service.AdminLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        AdminLogResponseDTO responseDTOS = adminLogRepository.search(searchRequestDTO, pageable);
        return responseDTOS;
    }
}
