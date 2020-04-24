package com.cogent.cogentappointment.logging.service;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Rupak
 */
public interface ClientLogService {

    UserMenuLogResponseDTO searchByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogStaticsByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsforDiagram(ClientLogSearchRequestDTO searchRequestDTO);

    UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsforDiagramByClientId(ClientLogSearchRequestDTO searchRequestDTO);
}
