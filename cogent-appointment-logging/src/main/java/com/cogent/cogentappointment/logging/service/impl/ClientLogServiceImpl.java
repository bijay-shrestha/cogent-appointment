package com.cogent.cogentappointment.logging.service.impl;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogStaticsResponseDTO;
import com.cogent.cogentappointment.logging.repository.ClientLogRepository;
import com.cogent.cogentappointment.logging.service.ClientLogService;
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
public class ClientLogServiceImpl implements ClientLogService {

    private final ClientLogRepository clientLogRepository;

    public ClientLogServiceImpl(ClientLogRepository clientLogRepository) {
        this.clientLogRepository = clientLogRepository;
    }

    @Override
    public AdminLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {

        AdminLogResponseDTO responseDTOS = clientLogRepository.search(searchRequestDTO, pageable);
        return responseDTOS;
    }

    @Override
    public List<AdminLogStaticsResponseDTO> fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO) {
        List<AdminLogStaticsResponseDTO> responseDTOS = clientLogRepository.fetchUserMenuLogsStatics(searchRequestDTO);
        return responseDTOS;
    }

}
