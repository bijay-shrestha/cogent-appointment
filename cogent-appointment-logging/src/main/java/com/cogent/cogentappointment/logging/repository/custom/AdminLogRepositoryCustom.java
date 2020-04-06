package com.cogent.cogentappointment.logging.repository.custom;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogSearchResponseDTO;

import java.util.List;

/**
 * @author Rupak
 */
public interface AdminLogRepositoryCustom {

    List<AdminLogSearchResponseDTO> search(AdminLogSearchRequestDTO searchRequestDTO);
}
