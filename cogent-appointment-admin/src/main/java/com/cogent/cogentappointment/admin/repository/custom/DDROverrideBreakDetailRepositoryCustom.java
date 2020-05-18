package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDROverrideBreakDetailResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 14/05/20
 */
@Repository
@Qualifier("ddrOverrideBreakDetailRepositoryCustom")
public interface DDROverrideBreakDetailRepositoryCustom {

    List<DDRBreakDetailResponseDTO> fetchExistingOverrideBreakDetail(Long ddrOverrideId);

    List<DDROverrideBreakDetailResponseDTO> fetchOverrideBreakDetail(Long ddrOverrideId);
}
