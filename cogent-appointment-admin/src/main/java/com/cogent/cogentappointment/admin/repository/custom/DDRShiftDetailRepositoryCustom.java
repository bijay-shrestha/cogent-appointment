package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRShiftMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRShiftDetailResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Repository
@Qualifier("ddrShiftDetailRepositoryCustom")
public interface DDRShiftDetailRepositoryCustom {

    List<DDRShiftMinResponseDTO> fetchMinShiftInfo(Long ddrId);

    Long validateDDRShiftCount(List<Long> shiftIds, Long ddrId);

    List<DDRShiftDetailResponseDTO> fetchShiftDetails(Long ddrId);

}
