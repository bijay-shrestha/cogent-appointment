package com.cogent.cogentappointment.logging.service;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Rupak
 */
public interface AdminLogService {
    
    AdminLogResponseDTO search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable);
}
