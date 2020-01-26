package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.response.admin.AdminMetaInfoResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
@Qualifier("adminMetaInfoRepositoryCustom")
public interface AdminMetaInfoRepositoryCustom {
    List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoResponseDTOS();
}
