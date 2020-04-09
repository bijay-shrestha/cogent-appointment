package com.cogent.cogentappointment.logging.repository.custom;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogStaticsResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Rupak
 */
public interface ClientLogRepositoryCustom {


    AdminLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    List<AdminLogStaticsResponseDTO> fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO);

}
