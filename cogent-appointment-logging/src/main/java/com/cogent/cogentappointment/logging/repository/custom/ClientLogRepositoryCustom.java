package com.cogent.cogentappointment.logging.repository.custom;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Rupak
 */
public interface ClientLogRepositoryCustom {

    UserMenuLogResponseDTO searchByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogStaticsByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogStaticsForDiagramByClientId(ClientLogSearchRequestDTO searchRequestDTO);

    UserMenuLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogStaticsForDiagram(ClientLogSearchRequestDTO searchRequestDTO);
}
