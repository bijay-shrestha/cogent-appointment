package com.cogent.cogentappointment.logging.service;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogSearchResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Rupak
 */
public interface AdminLogService {


    List<AdminLogSearchResponseDTO> search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable);
}
