package com.cogent.cogentappointment.logging.service;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Rupak
 */
public interface AdminLogService {

    UserMenuLogResponseDTO search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogStaticsForDiagram(AdminLogSearchRequestDTO searchRequestDTO);
}
