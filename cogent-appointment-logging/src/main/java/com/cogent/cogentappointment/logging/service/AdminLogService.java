package com.cogent.cogentappointment.logging.service;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogStaticsResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Rupak
 */
public interface AdminLogService {

    AdminLogResponseDTO search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(AdminLogSearchRequestDTO searchRequestDTO);
}
