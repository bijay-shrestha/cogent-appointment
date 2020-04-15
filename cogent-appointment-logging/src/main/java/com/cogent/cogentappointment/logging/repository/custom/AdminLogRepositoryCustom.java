package com.cogent.cogentappointment.logging.repository.custom;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author Rupak
 */
@Repository
@Qualifier("adminLogRepositoryCustom")
public interface AdminLogRepositoryCustom {

    AdminLogResponseDTO search(AdminLogSearchRequestDTO searchRequestDTO,Pageable pageable);

    UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(AdminLogSearchRequestDTO searchRequestDTO);
}
