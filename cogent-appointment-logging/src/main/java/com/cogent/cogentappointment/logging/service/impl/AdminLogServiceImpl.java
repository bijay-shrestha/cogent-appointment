package com.cogent.cogentappointment.logging.service.impl;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogSearchResponseDTO;
import com.cogent.cogentappointment.logging.repository.AdminLogRepository;
import com.cogent.cogentappointment.logging.service.AdminLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public List<AdminLogSearchResponseDTO> search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable) {
        return null;
    }
}
